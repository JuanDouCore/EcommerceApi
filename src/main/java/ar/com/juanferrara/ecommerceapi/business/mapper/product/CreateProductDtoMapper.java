package ar.com.juanferrara.ecommerceapi.business.mapper.product;

import ar.com.juanferrara.ecommerceapi.business.mapper.GenericMapper;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CreateProductDtoMapper extends GenericMapper<Product, CreateProductDTO> {
}
