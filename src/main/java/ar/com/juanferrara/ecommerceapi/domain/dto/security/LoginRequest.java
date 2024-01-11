package ar.com.juanferrara.ecommerceapi.domain.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @Email(message = "Please put a valid mail")
    @NotBlank(message = "The email can't be blank")
    @Schema(example = "user@gmail.com")
    private String email;

    @NotBlank(message = "Please put a password")
    @Schema(example = "password123")
    private String password;
}
