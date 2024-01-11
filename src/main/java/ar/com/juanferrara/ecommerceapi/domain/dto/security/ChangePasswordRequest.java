package ar.com.juanferrara.ecommerceapi.domain.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

public class ChangePasswordRequest {

    @Email(message = "The email is not valid")
    @Schema(example = "user@gmail.com")
    private String email;

    @Size(min = 8, message = "The password size must be at least 8 characters")
    @Schema(example = "currentpassword123")
    private String currentPassword;

    @Size(min = 8, message = "The password size must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$)", message = "Password does not meet requirements")
    @Schema(example = "newpassword123")
    private String newPassword;
}
