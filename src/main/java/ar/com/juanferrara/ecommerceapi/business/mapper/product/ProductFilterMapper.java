package ar.com.juanferrara.ecommerceapi.business.mapper.product;

import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductFilter;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface ProductFilterMapper {

    @Mapping(target = "id", source = "productFilter.maxPrice")
    Product toEntity(ProductFilter productFilter);
}
