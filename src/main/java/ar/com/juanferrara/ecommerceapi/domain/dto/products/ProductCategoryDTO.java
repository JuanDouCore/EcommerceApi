package ar.com.juanferrara.ecommerceapi.domain.dto.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductCategoryDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "netbooks")
    private String name;

    @Schema(example = "computadoras tipo netbook y notebook")
    private String description;
}
