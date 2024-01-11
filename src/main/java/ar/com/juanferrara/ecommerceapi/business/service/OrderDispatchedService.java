package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDispatchedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDispatchedService {

    OrderDispatchedDTO dispatchOrder(CreateOrderDispatchedDTO createOrderDispatchedDTO);
    OrderDispatchedDTO getOrderDispatchedById(Long id);
    OrderDispatchedDTO getOrderDispatechedByOrderId(Long orderId);
    PageResponse getAllOrderdsDispatchedByEmployee(Long dniEmployee, Pageable pageable);
    PageResponse getAllOrdersDispatchedOfAnyClient(Long dniClient, Pageable pageable);
    PageResponse getAllOrderdsDispatched(Pageable pageable);

}
