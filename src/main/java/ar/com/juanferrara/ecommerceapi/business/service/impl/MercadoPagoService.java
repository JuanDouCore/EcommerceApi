package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import ar.com.juanferrara.ecommerceapi.persistence.OrderRepository;
import ar.com.juanferrara.ecommerceapi.presentation.controller.IpnPaymentController;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    Logger logger = LoggerFactory.getLogger(IpnPaymentController.class);

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private OrderRepository orderRepository;


    public String createOrderPayment(Order order)  {
        MercadoPagoConfig.setAccessToken(accessToken);

        List<PreferenceItemRequest> preferenceItemRequestList = order.getItems().stream()
                .map(orderItem -> PreferenceItemRequest.builder()
                        .id(orderItem.getProduct().getId().toString())
                        .title(orderItem.getProduct().getName())
                        .currencyId("ARS")
                        .description(orderItem.getProduct().getDescription())
                        .pictureUrl(orderItem.getProduct().getImageUrl())
                        .categoryId(orderItem.getProduct().getCategory().getName())
                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getProduct().getPrice())
                        .build()).toList();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://www.success.com")
                .failure("https://www.success.com")
                .pending("https://www.success.com")
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(preferenceItemRequestList)
                .metadata(Map.of(
                        "purcharse_reference", order.getOrderReferenceExternal()
                ))
                .marketplace("ECOMMERCE API")
                .notificationUrl(serverUrl + "/api/ipn/mercadopago/notify?source_news=webhooks")
                .statementDescriptor("Compra #" + order.getId())
                .expires(true)
                .expirationDateFrom(OffsetDateTime.now())
                .expirationDateTo(OffsetDateTime.now().plus(Duration.ofHours(24)))
                .build();

        Preference preference;
        PreferenceClient preferenceClient = new PreferenceClient();
        try {
            preference = preferenceClient.create(preferenceRequest);
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }

        PaymentClient paymentClient = new PaymentClient();
        return  preference.getSandboxInitPoint();
    }


    public boolean processNotificationWebhook2(Map<String, Object> data) {
        if(data.containsKey("type") &&  data.containsKey("action") && data.containsKey("data")) {
            Object dataObject= data.get("data");

            if(dataObject instanceof Map) {
                Map<String, Object> dataValues = (Map<String, Object>) dataObject;
                String idPayment = (String) dataValues.get("id");

                if(idPayment != null) {
                    MercadoPagoConfig.setAccessToken(accessToken);
                    PaymentClient paymentClient = new PaymentClient();

                    try {
                        Payment payment = paymentClient.get(Long.valueOf(idPayment));

                        if(payment.getStatus().equals("approved") && payment.getStatusDetail().equals("accredited")) {
                            Order order = orderRepository.findByOrderReferenceExternal((String) payment.getMetadata().get("purcharse_reference"))
                                            .orElseThrow(() -> new RuntimeException("An error ocurred trying to find a order with external reference " + (String) payment.getMetadata().get("purcharse_reference")));

                            if(payment.getTransactionDetails().getTotalPaidAmount().compareTo(order.getTotalCost()) > 0 || payment.getTransactionDetails().getTotalPaidAmount().compareTo(order.getTotalCost()) == 0) {
                                order.setPaymentStatus(PaymentStatus.APROBADO);
                                order.setStatus(OrderStatus.PENDIENTE_ENVIO);
                                order.setLastUpdate(new Date());

                                orderRepository.save(order);
                            }
                            return true;
                        }
                    } catch (MPException | MPApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return false;
    }

    public boolean processNotificationWebhook(Map<String, Object> data) {
        if (isValidNotificationData(data)) {
            String idPayment = extractPaymentId(data);

            if (idPayment != null) {
                try {
                    Payment payment = getPaymentById(idPayment);
                    if (isPaymentApproved(payment)) {
                        updateOrderStatus(payment);
                        return true;
                    }
                } catch (MPException | MPApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    private boolean isValidNotificationData(Map<String, Object> data) {
        return data.containsKey("type") && data.containsKey("action") && data.containsKey("data");
    }

    private String extractPaymentId(Map<String, Object> data) {
        Object dataObject = data.get("data");
        if (dataObject instanceof Map) {
            Map<String, Object> dataValues = (Map<String, Object>) dataObject;
            return (String) dataValues.get("id");
        }
        return null;
    }

    private Payment getPaymentById(String idPayment) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient paymentClient = new PaymentClient();
        return paymentClient.get(Long.valueOf(idPayment));
    }

    private boolean isPaymentApproved(Payment payment) {
        return payment.getStatus().equals("approved") && payment.getStatusDetail().equals("accredited");
    }

    private void updateOrderStatus(Payment payment) {
        String purchaseReference = (String) payment.getMetadata().get("purcharse_reference");
        Order order = orderRepository.findByOrderReferenceExternal(purchaseReference)
                .orElseThrow(() -> new RuntimeException("An error occurred trying to find an order with external reference " + purchaseReference));

        if (payment.getTransactionDetails().getTotalPaidAmount().compareTo(order.getTotalCost()) >= 0) {
            order.setPaymentStatus(PaymentStatus.APROBADO);
            order.setStatus(OrderStatus.PENDIENTE_ENVIO);
            order.setLastUpdate(new Date());

            orderRepository.save(order);
        }
    }

}
