package ar.com.juanferrara.ecommerceapi.domain.dto.products;

import ar.com.juanferrara.ecommerceapi.domain.entity.ProductCategory;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductFilter {

    private String name;
    private String categoryName;
    private Double maxPrice;
    private OrderBy orderBy;
}
