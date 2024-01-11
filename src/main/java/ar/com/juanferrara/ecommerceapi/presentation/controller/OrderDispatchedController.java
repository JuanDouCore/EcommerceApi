package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.OrderDispatchedService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDispatchedDTO;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderDate;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders/dispatch")

public class OrderDispatchedController {

    @Autowired
    private OrderDispatchedService orderDispatchedService;

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "dispatchOrder",
            summary = "Despachar una orden",
            description = "Despachar una orden que su estado se encuentre ya en PENDIENTE_ENVIO con el pago APROBADO \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de orden a ser despachada"
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Orden despachada", content = @Content(schema = @Schema(implementation = OrderDispatchedDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada | Empleado no encontrado", content = @Content),
                    @ApiResponse(responseCode = "409", description = "La orden no se encuentra en estado PENDIENTE_ENVIO", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<OrderDispatchedDTO> dispatchOrder(@RequestBody @Valid CreateOrderDispatchedDTO createOrderDispatchedDTO) {
        OrderDispatchedDTO orderDispatched = orderDispatchedService.dispatchOrder(createOrderDispatchedDTO);
        return ResponseEntity.created(URI.create("/api/orders/dispatch/" + orderDispatched.getId())).body(orderDispatched);
    }

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "getOrderDispatchedById",
            summary = "Buscar un despacho de orden por id",
            description = "Buscar un despacho de orden por id \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despacho de orden encontrado", content = @Content(schema = @Schema(implementation = OrderDispatchedDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Despacho de orden no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("{id}")
    public ResponseEntity<OrderDispatchedDTO> getOrderDispatchedById(@PathVariable Long id) {
        return ResponseEntity.ok(orderDispatchedService.getOrderDispatchedById(id));
    }

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "getOrderDispatchedByOrderId",
            summary = "Buscar un despacho de orden por id de orden",
            description = "Buscar un despacho de orden por id de orden \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despacho de orden encontrado", content = @Content(schema = @Schema(implementation = OrderDispatchedDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Despacho de orden no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("order/{orderId}")
    public ResponseEntity<OrderDispatchedDTO> getOrderDispatchedByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDispatchedService.getOrderDispatechedByOrderId(orderId));
    }

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "getAllOrderdsDispatchedByEmployee",
            summary = "Listar todos los despachos de ordenes despachadas por un empleado",
            description = "Listar todos los despachos de ordenes despachadas por un empleado \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("employee/{dniEmployee}")
    public ResponseEntity<PageResponse> getAllOrderdsDispatchedByEmployee(@PathVariable Long dniEmployee,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "MAS_RECIENTES") OrderDate orderDate) {

        Pageable pageable;
        if(orderDate.equals(OrderDate.MAS_RECIENTES))
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").descending());
        else
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").ascending());

        return ResponseEntity.ok(orderDispatchedService.getAllOrderdsDispatchedByEmployee(dniEmployee, pageable));
    }

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "getAllOrdersDispatchedOfAnyClient",
            summary = "Listar todos los despachos de ordenes despachadas de un cliente",
            description = "Listar todos los despachos de ordenes despachadas de un cliente \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("client/{dniClient}")
    public ResponseEntity<PageResponse> getAllOrderdsDispatchedOfAnyClient(@PathVariable Long dniClient,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size,
                                                                                      @RequestParam(defaultValue = "MAS_RECIENTES") OrderDate orderDate) {

        Pageable pageable;
        if(orderDate.equals(OrderDate.MAS_RECIENTES))
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").descending());
        else
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").ascending());

        return ResponseEntity.ok(orderDispatchedService.getAllOrdersDispatchedOfAnyClient(dniClient, pageable));
    }

    @Operation(
            tags = {"Despacho de ordenes"},
            operationId = "getAllOrderdsDispatched",
            summary = "Listar todos los despachos de ordenes despachadas",
            description = "Listar todos los despachos de ordenes despachadas \n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE + "\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<PageResponse> getAllOrderdsDispatched(@RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size,
                                                                                       @RequestParam(defaultValue = "MAS_RECIENTES") OrderDate orderDate) {

        Pageable pageable;
        if(orderDate.equals(OrderDate.MAS_RECIENTES))
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").descending());
        else
            pageable = PageRequest.of(page, size, Sort.by("dateDispatched").ascending());

        return ResponseEntity.ok(orderDispatchedService.getAllOrderdsDispatched(pageable));
    }
}
