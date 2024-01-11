package ar.com.juanferrara.ecommerceapi.domain.dto.security;

import ar.com.juanferrara.ecommerceapi.domain.dto.address.AddressDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterRequest {

    @Email(message = "The mail must be valid")
    @NotBlank(message = "The email can't be blank")
    @Schema(example = "user@gmail.com")
    private String email;

    @Size(min = 8, message = "The password size must be at least 8 characters")
    @Schema(example = "password123")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$)", message = "Password does not meet requirements")
    private String password;

    @Positive(message = "The dni must be positive")
    @Min(value = 7,  message = "The DNI have at least 7 digits")
    @Schema(example = "22000333")
    private Long dni;

    @NotBlank(message = "The names can't be blank")
    @Size(min =3, max = 60, message = "The names size must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "Name")
    private String names;

    @NotBlank(message = "The last name can't be blank")
    @Size(min =3, max = 60, message = "The last name size must be between 3 and 45 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "Lastname")
    private String lastName;

    @Schema(example = "11/01/1998")
    private String birthDate;

    @NotNull(message = "The address can't be null")
    private AddressDTO address;
}
