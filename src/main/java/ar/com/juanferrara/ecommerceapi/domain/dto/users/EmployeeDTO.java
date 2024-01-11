package ar.com.juanferrara.ecommerceapi.domain.dto.users;

import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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

public class EmployeeDTO {

    @Schema(example = "2")
    private Long userId;

    @Schema(example = "11222333")
    private Long dni;

    @Schema(example = "user@gmail.com")
    private String email;

    @Schema(example = "Jorge")
    private String names;

    @Schema(example = "Lopez")
    private String lastName;

    @Schema(example = "EMPLEADO")
    private UserRole role;

}
