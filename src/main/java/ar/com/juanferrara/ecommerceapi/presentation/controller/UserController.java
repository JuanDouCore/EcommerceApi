package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.ClientService;
import ar.com.juanferrara.ecommerceapi.business.service.EmployeeService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.address.AddressDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Address;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmployeeService employeeService;

    @Operation(
            tags = {"Usuarios"},
            operationId = "listAllClients",
            summary = "Listar todos los clientes",
            description = "Lista todos los clientes\n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("clients")
    public ResponseEntity<PageResponse> listAllClients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(clientService.listAllClients(PageRequest.of(page, size)));
    }

    @Operation(
            tags = {"Usuarios"},
            operationId = "findClients",
            summary = "Buscar clientes",
            description = "Busca clientes por dni, nombre o apellido\n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("clients/find")
    public ResponseEntity<PageResponse> findClients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) Long dni,
                                                    @RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName) {

        return ResponseEntity.ok(clientService.findClients(PageRequest.of(page, size), ClientDTO.builder()
                .dni(dni)
                .lastName(lastName)
                .names(firstName)
                .build()));
    }

    @Operation(
            tags = {"Usuarios"},
            operationId = "getAddressesOfClient",
            summary = "Direcciones de un cliente por id",
            description = "Buscar las direcciones asociadas a un cliente\n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR') || isAuthenticated() && principal.id == #idClient")
    @GetMapping("clients/{idClient}/addresses")
    public ResponseEntity<Set<Address>> getAddressesOfClient(@PathVariable Long idClient) {
        return ResponseEntity.ok(clientService.getAddressesOfClient(idClient));
    }

    @Operation(
            tags = {"Usuarios"},
            operationId = "listAllEmployees",
            summary = "Listar todos los empleados",
            description = "Lista todos los empleados\n" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @GetMapping("employees")
    public ResponseEntity<PageResponse> listAllEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(employeeService.listAllEmployees(PageRequest.of(page, size)));
    }

    @Operation(
            tags = {"Usuarios"},
            operationId = "findEmployees",
            summary = "Buscar empleados",
            description = "Busca empleados por dni, nombre o apellido\n" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @GetMapping("employees/find")
    public ResponseEntity<PageResponse> findEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) Long dni,
                                                    @RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName) {

        return ResponseEntity.ok(employeeService.findEmployees(PageRequest.of(page, size), EmployeeDTO.builder()
                .dni(dni)
                .lastName(lastName)
                .names(firstName)
                .build()));
    }

    @Operation(
            tags = {"Usuarios"},
            operationId = "changeEmployeeRole",
            summary = "Cambiar el rol de un empleado",
            description = "Cambiar el rol de un empleado\n" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PatchMapping("employees/{dniEmployee}/changerole")
    public ResponseEntity<EmployeeDTO> changeRoleEmployee(@PathVariable Long dniEmployee, @RequestParam UserRole newRole) {
        return ResponseEntity.ok(employeeService.changeEmployeeRole(dniEmployee, newRole));
    }
}
