package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.PageResponseMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.order.OrderDispatchedDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.service.OrderDispatchedService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import ar.com.juanferrara.ecommerceapi.domain.entity.OrderDispatched;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.EmployeeRepository;
import ar.com.juanferrara.ecommerceapi.persistence.OrderDispatchedRepository;
import ar.com.juanferrara.ecommerceapi.persistence.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderDispatchedServiceImpl implements OrderDispatchedService {

    @Autowired
    private OrderDispatchedRepository orderDispatchedRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrderDispatchedDtoMapper orderDispatchedDtoMapper;


    @Override
    public OrderDispatchedDTO dispatchOrder(CreateOrderDispatchedDTO createOrderDispatchedDTO) {
        Order order = orderRepository.findById(createOrderDispatchedDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("No order found with this id"));

        Employee employeeDispatcher = employeeRepository.findById(createOrderDispatchedDTO.getDniEmployeeDispatcher())
                .orElseThrow(() -> new NotFoundException("No employee found with this dni"));

        if(order.getStatus() != OrderStatus.PENDIENTE_ENVIO)
            throw new GenericException("ORDER STATUS", "This order not have status PENDIENTE_ENVIO", HttpStatus.CONFLICT);

        OrderDispatched orderDispatched = OrderDispatched.builder()
                .order(order)
                .employee(employeeDispatcher)
                .dateDispatched(new Date())
                .shippingCompany(createOrderDispatchedDTO.getShippingCompany())
                .trackingCode(createOrderDispatchedDTO.getTrackingCode())
                .build();

        order.setStatus(OrderStatus.COMPLETADO);
        order.setLastUpdate(new Date());
        orderRepository.save(order);

        return orderDispatchedDtoMapper.toDto(orderDispatchedRepository.save(orderDispatched));
    }

    @Override
    public OrderDispatchedDTO getOrderDispatchedById(Long id) {
        return orderDispatchedDtoMapper.toDto(orderDispatchedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No order dispatched found with this id")));
    }

    @Override
    public OrderDispatchedDTO getOrderDispatechedByOrderId(Long orderId) {
        return orderDispatchedDtoMapper.toDto(orderDispatchedRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("No order found with this id")));
    }

    @Override
    public PageResponse getAllOrderdsDispatchedByEmployee(Long dniEmployee, Pageable pageable) {
        Page<OrderDispatched> orderDispatches = orderDispatchedRepository.findAllByEmployeeDni(dniEmployee, pageable);
        return PageResponseMapper.convertToPageResponse(orderDispatches.map(orderDispatchedDtoMapper::toDto));
    }

    @Override
    public PageResponse getAllOrdersDispatchedOfAnyClient(Long dniClient, Pageable pageable) {
        Page<OrderDispatched> orderDispatches = orderDispatchedRepository.findAllByOrderClientDni(dniClient, pageable);
        return PageResponseMapper.convertToPageResponse(orderDispatches.map(orderDispatchedDtoMapper::toDto));
    }

    @Override
    public PageResponse getAllOrderdsDispatched(Pageable pageable) {
        Page<OrderDispatched> orderDispatches = orderDispatchedRepository.findAll(pageable);
        return PageResponseMapper.convertToPageResponse(orderDispatches.map(orderDispatchedDtoMapper::toDto));
    }
}
