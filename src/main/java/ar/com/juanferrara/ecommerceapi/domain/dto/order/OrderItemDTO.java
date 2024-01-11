package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderItemDTO {


    private ProductInfoDTO  product;

    @Schema(example = "2")
    private Integer quantity;

    @Schema(example = "200000")
    private BigDecimal totalCost;
}
