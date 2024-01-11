package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateOrderItemDTO {

    @Positive(message = "The product id must be valid")
    @Schema(example = "5")
    private Long productId;

    @Positive(message = "The quantity must be positive")
    @Schema(example = "2")
    private Integer quantity;
}
