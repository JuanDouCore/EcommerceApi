package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderDate;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    OrderDTO createOrder(CreateOrderDTO createOrderDTO);
    OrderDTO getOrderById(Long orderId);
    OrderDTO getOrderByIdAndUserId(Long orderId, Long userId);
    PageResponse getOrdersByUserId(Long userId, Pageable pageable);
    PageResponse getAllOrders(Pageable pageable);
    PageResponse getAllOrdersWithStatus(OrderStatus status, Pageable pageable);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderDTO updateOrderPaymentStatus(Long orderId, PaymentStatus newStatus);
    OrderDTO deleteOrder(Long orderId);
}
