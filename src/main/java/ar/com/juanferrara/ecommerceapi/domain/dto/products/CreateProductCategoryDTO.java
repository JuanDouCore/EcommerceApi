package ar.com.juanferrara.ecommerceapi.domain.dto.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateProductCategoryDTO {

    @NotBlank(message = "Name category is required")
    @Size(max = 50, message = "The name category must have a maximum of 50 characters")
    @Schema(example = "netbooks")
    private String name;

    @NotBlank(message = "Description category is required")
    @Size(max = 120, message = "The description category must have a maximum of 120 characters")
    @Schema(example = "computadoras tipo netbook y notebook")
    private String description;

}
