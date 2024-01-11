package ar.com.juanferrara.ecommerceapi.domain.dto.users;

import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDTO {
    private Long id;
    private String email;
    private UserRole role;
    private boolean enabled;
}
