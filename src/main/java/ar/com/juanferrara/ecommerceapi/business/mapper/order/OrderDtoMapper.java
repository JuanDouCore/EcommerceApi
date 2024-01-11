package ar.com.juanferrara.ecommerceapi.business.mapper.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderDtoMapper {

    @Mapping(target = "clientUserId", source = "order.client.user.id")
    OrderDTO toOrderDto(Order order);

    @Mapping(target = "clientUserId", source = "order.client.user.id")
    List<OrderDTO> toOrderDtoList(List<Order> orders);
}
