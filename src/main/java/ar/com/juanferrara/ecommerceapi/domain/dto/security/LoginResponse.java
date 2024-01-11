package ar.com.juanferrara.ecommerceapi.domain.dto.security;

import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse<UserType> {

    @Schema(implementation = ClientDTO.class)
    private UserType userType;

    @Schema(example = "Bearer")
    private String tokenType;

    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2wiOiJDTElFTlRFIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQ0xJRU5URSJ9XSwidXNlcm5hbWUiOiJqdWFuam95cXVlY28uOEBob3RtYWlsLmNvbSIsImlhdCI6MTcwNDc0NjY0NCwiZXhwIjoxNzA0NzQ4NDQ0fQ.0PuV-ZZc_9ZbnqWg_ydtS1Wn-M4qntyfu112_4Kl1iI")
    private String token;

    @Schema(example = "02393d98-1739-4451-a3c5-fc6507db46e7")
    private String refreshToken;
}
