package ar.com.juanferrara.ecommerceapi.domain.dto.security;

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

public class RegisterEmployeeRequest {

    @NotBlank(message = "The email can't be blank")
    @Email(message = "The mail must be valid")
    @Schema(example = "user@gmail.com")
    private String email;

    @Size(min = 8, message = "The password size must be at least 8 characters")
    @Schema(example = "password123")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$)", message = "Password does not meet requirements")
    private String password;

    @Positive(message = "The dni must be positive")
    @Min(value = 7,  message = "The DNI have at least 7 digits")
    @Schema(example = "11222333")
    private Long dni;

    @NotBlank(message = "The names can't be blank")
    @Size(min =3, max = 60, message = "The names size must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "Jorge")
    private String names;

    @NotBlank(message = "The last name can't be blank")
    @Size(min =3, max = 60, message = "The last name size must be between 3 and 45 characters")
    @Pattern(regexp = "^[A-Za-zÁáÉéÍíÓóÚúÜüÑñ]+$", message = "Please dont use invalid characters")
    @Schema(example = "Lopez")
    private String lastName;
}
