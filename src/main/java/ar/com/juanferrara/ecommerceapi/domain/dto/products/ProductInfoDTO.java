package ar.com.juanferrara.ecommerceapi.domain.dto.products;

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

public class ProductInfoDTO {

    @Schema(example = "5")
    private Long id;

    @Schema(example = "Auriculares HyperX")
    private String name;

    @Schema(example = "de gran calidad con gran sonido")
    private String description;

    private ProductCategoryDTO category;

    @Schema(example = "image url product")
    private String imageUrl;

    @Schema(example = "100000")
    private BigDecimal price;
}
