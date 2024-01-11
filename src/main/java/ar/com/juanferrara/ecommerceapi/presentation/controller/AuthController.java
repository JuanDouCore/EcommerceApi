package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.AuthService;
import ar.com.juanferrara.ecommerceapi.business.service.RefreshTokenService;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.*;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")

@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Operation(
            tags = {"Autenticacion"},
            operationId = "authRegister",
            summary = "Registrarse",
            description = "Registrarse como cliente nuevo",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de registro"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente registrado", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Formato de fecha invalida (dd/MM/yyyy)", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Ya existe un cliente con este dni o email", content = @Content)
            }
    )
    @PostMapping("register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            tags = {"Autenticacion"},
            operationId = "authLogin",
            summary = "Iniciar sesion",
            description = "Iniciar sesion",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de inicio de sesion"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sesion autorizada", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Credenciales invalidas", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
            }
    )
    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            tags = {"Autenticacion"},
            operationId = "authRefreshToken",
            summary = "Refrescar token",
            description = "Refrescar token para obtener de nuevo un JWT y evitar la expiracion pronta. Se recomienda refrescar siempre antes de los 120 minutos o este expirará." +
                    "\n\n" + Constants.BADGED_CLIENT +"\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de inicio de sesion"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refrescado", content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Token expirado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Token invalido", content = @Content)
            }
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

    @Operation(
            tags = {"Autenticacion"},
            operationId = "authChangePassword",
            summary = "Cambiar contraseña",
            description = "Cambiar su contraseña." +
                    "\n\n" + Constants.BADGED_CLIENT +"\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de cambio de contraseña"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña cambiada", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Contraseña invalida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
            }
    )
    @PreAuthorize("principal.id == #userId || hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping("changepassword/{userId}")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changepassword(request);
        return ResponseEntity.ok("changed");
    }

    @Operation(
            tags = {"Autenticacion"},
            operationId = "authRegisterEmployee",
            summary = "Registrar empleado",
            description = "Registrar un nuevo empleado" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de empleado"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empleado registrado", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Ya existe un empleado con este dni o email", content = @Content),
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping("register/employee")
    public ResponseEntity<EmployeeDTO> registerEmployee(@Valid @RequestBody RegisterEmployeeRequest request) {
        return ResponseEntity.ok(authService.registerEmployee(request));
    }

}
