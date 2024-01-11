package ar.com.juanferrara.ecommerceapi.business.mapper.order;


import ar.com.juanferrara.ecommerceapi.business.mapper.GenericMapper;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import ar.com.juanferrara.ecommerceapi.domain.entity.OrderDispatched;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderDispatchedDtoMapper {

    @Mapping(target = "order.clientUserId", source = "order.client.user.id")
    @Mapping(target = "employee.userId", source = "order.employee.user.id")
    @Mapping(target = "employee.email", source = "order.employee.user.email")
    @Mapping(target = "employee.role", source = "order.employee.user.role")
    OrderDispatchedDTO toDto(OrderDispatched order);

    @Mapping(target = "order.clientUserId", source = "orders.client.user.id")
    @Mapping(target = "employee.userId", source = "orders.employee.user.id")
    @Mapping(target = "employee.email", source = "orders.employee.user.email")
    @Mapping(target = "employee.role", source = "orders.employee.user.role")
    List<OrderDispatchedDTO> toOrderDtoList(List<OrderDispatched> orders);
}
