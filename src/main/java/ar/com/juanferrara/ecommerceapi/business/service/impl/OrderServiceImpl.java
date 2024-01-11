package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.PageResponseMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.order.OrderDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.service.OrderService;
import ar.com.juanferrara.ecommerceapi.business.service.ProductService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.*;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderDate;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.AddressRepository;
import ar.com.juanferrara.ecommerceapi.persistence.ClientRepository;
import ar.com.juanferrara.ecommerceapi.persistence.OrderRepository;
import ar.com.juanferrara.ecommerceapi.persistence.ProductRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private OrderDtoMapper orderDtoMapper;

    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Client client = clientRepository.findByUserId(createOrderDTO.getClientUserId())
                .orElseThrow(() -> new GenericException("CLIENT NOT FOUND",
                        "Client whit this user id not found",
                        HttpStatus.NOT_FOUND));

        Address shippingAddress = addressRepository.findById(createOrderDTO.getShippingAddressId())
                .orElseThrow(() -> new GenericException("ADDRESS NOT FOUND",
                        "Address whit this id not found",
                        HttpStatus.NOT_FOUND));

        Order order = Order.builder()
                .client(client)
                .shippingAddress(shippingAddress)
                .status(OrderStatus.PENDIENTE_PAGO)
                .paymentStatus(PaymentStatus.PENDIENTE)
                .dateCreated(new Date())
                .lastUpdate(new Date())
                .orderReferenceExternal(UUID.randomUUID().toString())
                .build();

        Set<OrderItem> orderItems = createOrderDTO.getItems().stream()
                .map(orderItem -> {
                    Product product = productRepository.findById(orderItem.getProductId())
                            .orElseThrow(() -> new GenericException(
                                    "PRODUCT NOT FOUND",
                                    "Product with id " + orderItem.getProductId() + " not found",
                                    HttpStatus.NOT_FOUND));

                    if (product.getStock() >= orderItem.getQuantity()) {
                        productService.removeStockOfProduct(product.getId(), orderItem.getQuantity());
                        return OrderItem.builder()
                                .quantity(orderItem.getQuantity())
                                .product(product)
                                .totalCost(BigDecimal.valueOf(orderItem.getQuantity()).multiply(product.getPrice()))
                                .build();
                    } else {
                        throw new GenericException("NO STOCK PRODUCT", "Insufficient stock for the product " + product.getId(), HttpStatus.CONFLICT);
                    }
                })
                .collect(Collectors.toSet());

        BigDecimal totalCost = orderItems.stream()
                .map(OrderItem::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(orderItems);
        order.setTotalCost(totalCost);
        order.setPreferenceIdPaymentMPago(mercadoPagoService.createOrderPayment(order));

        return orderDtoMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        return orderDtoMapper.toOrderDto(order);
    }

    @Override
    public OrderDTO getOrderByIdAndUserId(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        if(order.getClient().getUser().getId() != userId)
            throw new GenericException("ORDER USER ERROR", "It can only be viewed by the owner user", HttpStatus.BAD_REQUEST);

        return orderDtoMapper.toOrderDto(order);
    }

    @Override
    public PageResponse getOrdersByUserId(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByClientUserId(userId, pageable);
        return PageResponseMapper.convertToPageResponse(orders.map(orderDtoMapper::toOrderDto));
    }

    @Override
    public PageResponse getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return PageResponseMapper.convertToPageResponse(orders.map(orderDtoMapper::toOrderDto));
    }

    @Override
    public PageResponse getAllOrdersWithStatus(OrderStatus orderStatus, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByStatus(orderStatus, pageable);
        return PageResponseMapper.convertToPageResponse(orders.map(orderDtoMapper::toOrderDto));
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        order.setStatus(newStatus);
        order.setLastUpdate(new Date());
        orderRepository.save(order);

        return orderDtoMapper.toOrderDto(order);
    }

    @Override
    public OrderDTO updateOrderPaymentStatus(Long orderId, PaymentStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        order.setPaymentStatus(newStatus);
        order.setLastUpdate(new Date());
        orderRepository.save(order);

        return orderDtoMapper.toOrderDto(order);
    }


    @Override
    public OrderDTO deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        orderRepository.delete(order);

        return orderDtoMapper.toOrderDto(order);
    }
}
