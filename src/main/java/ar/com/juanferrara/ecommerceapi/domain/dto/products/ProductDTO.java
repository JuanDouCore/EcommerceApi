package ar.com.juanferrara.ecommerceapi.domain.dto.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "HP W10 t80")
    private String name;

    @Schema(example = "Windows 10 - 128 gb ssd - intel i7")
    private String description;

    private ProductCategoryDTO category;

    @Schema(example = "null")
    private String imageUrl;

    @Schema(example = "785212.21")
    private BigDecimal price;

    @Schema(example = "5")
    private Integer stock;

}
