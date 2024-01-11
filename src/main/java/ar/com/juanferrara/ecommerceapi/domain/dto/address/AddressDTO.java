package ar.com.juanferrara.ecommerceapi.domain.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AddressDTO {

    @NotBlank(message = "The street can't be blank")
    @Size(min = 3, max = 60, message = "The street must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ0-9]+$", message = "Please dont use invalid characters")
    @Schema(example = "Av. 9 de Julio 112031")
    private String street;

    @NotBlank(message = "The city can't be blank")
    @Size(min = 3, max = 60, message = "The city must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "Ciudad de Buenos Aires")
    private String city;

    @NotBlank(message = "The province can't be blank")
    @Size(min = 3, max = 45, message = "The province must be between 3 and 45 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
   @Schema(example = "Buenos Aires")
    private String province;

    @NotBlank(message = "The zip code can't be blank")
    @Size(min = 3, max = 10, message = "The zip code must be between 3 and 10 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ0-9]+$", message = "Please dont use invalid characters")
    @Schema(example = "1122B")
    private String zipCode;
}
