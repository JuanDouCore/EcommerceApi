package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.address.AddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateOrderDTO {

    @Positive(message = "The client id must be valid")
    @Schema(example = "1")
    private Long clientUserId;

    @Positive(message = "The shipping address must be valid")
    @Schema(example = "1")
    private Long shippingAddressId;

    @NotEmpty(message = "The order items can't be empty")
    private Set<CreateOrderItemDTO> items;
}
