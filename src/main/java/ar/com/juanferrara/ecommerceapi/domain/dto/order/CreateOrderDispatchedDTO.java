package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateOrderDispatchedDTO {

    @Positive(message = "The order id must be greater than 0")
    @Schema(example = "1")
    private Long orderId;

    @Positive(message = "The order id must be greater than 0")
    @Schema(example = "11222333")
    private Long dniEmployeeDispatcher;

    @NotBlank(message = "The shipping company must be valid")
    @Size(max = 45, message = "The shipping company cannot be more than 45 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "ANDREANI")
    private String shippingCompany;

    @NotBlank(message = "The tracking code must be valid")
    @Size(max = 45, message = "The tracking code cannot be more than 100 characters")
    @Schema(example = "ZZEEE221331EEDDDFFF")
    private String trackingCode;
}
