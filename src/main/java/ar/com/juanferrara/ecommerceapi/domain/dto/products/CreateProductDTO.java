package ar.com.juanferrara.ecommerceapi.domain.dto.products;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

//@Schema(description = "Producto a ser creado")
public class CreateProductDTO {

    @NotBlank(message = "The name is required")
    @Size(max = 60, message = "The name must be less than 60 characters")
    @Schema(example = "HP W10 t80")
    private String name;

    @NotBlank(message = "The description is required")
    @Size(max = 255, message = "The description must be less than 255 characters")
    @Schema(example = "Windows 10 - 128 gb ssd - intel i7")
    private String description;

    @Positive(message = "The category is required")
    @Schema(example = "1")
    private Long categoryId;

    @NotNull(message = "The price is required")
    @Positive(message = "The price must be greater than 0")
    @Schema(example = "785212.21")
    private BigDecimal price;

    @NotNull(message = "The stock is required")
    @Positive(message = "The stock must be greater than 0")
    @Schema(example = "5")
    private Integer stock;
}
