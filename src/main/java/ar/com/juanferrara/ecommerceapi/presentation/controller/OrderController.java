package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.OrderService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.CreateOrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderDate;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.codehaus.plexus.util.dag.DAG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private boolean isInvalidPageRequest(int page, int size) {
        return (page < 0 || size < 0 || size > 50);
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "createOrder",
            summary = "Crear una orden",
            description = "Crea una orden asignadole el id del cliente que esta creando la orden," +
                    " junto con el id de su direccion donde quiera que sea enviada. Por otro lado la carga de procutos es por id" +
                    "junto con la cantidad de ese producto que se quiera ordenar.\n\n" +
                    "**Si la orden se crea satisfactoriamente en el cuerpo de su respuesta contendrá un link de MercadoPago asociado para que esta orden sea abonada**" +
                    "\n\n" + Constants.BADGED_CLIENT +"\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la orden a ser creada"
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Orden creada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado\nDireccion no encontrada\nProducto/s inexistente", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Producto/s sin stock suficiente", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') || isAuthenticated() && principal.id == #createOrderDTO.clientUserId")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO){
        OrderDTO createdOrder = orderService.createOrder(createOrderDTO);
        return ResponseEntity.created(URI.create("/api/orders/" + createdOrder.getId())).body(createdOrder);
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "getOrderById",
            summary = "Obtiene una orden por su ID",
            description = "Obteien una orden por su ID. Exclusivo para buscar todas las ordenes existentes" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orden encontrada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping("{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "getOrderById",
            summary = "Obtiene una orden por usuario asociado junton con ID",
            description = "De utilidad para que los clientes busquen y observen exclusivamente y detalladamente sus ordenes asociadas" +
                    "\n\n" + Constants.BADGED_CLIENT ,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orden encontrada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
            }
    )
    @PreAuthorize("isAuthenticated() && #userId == principal.id")
    @GetMapping("user/{userId}/{orderId}")
    public ResponseEntity<OrderDTO> getOrderOfUserById(@PathVariable Long userId,@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByIdAndUserId(orderId, userId));
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "getOrdersByUserid",
            summary = "Listar todas las ordenes asociadas a un cliente",
            description = "Lista todas las ordenes asociadas a un cliente. Los clientes pueden utilizar este recurso para listar exclusivamente sus ordenes asociadas" +
                    "\n\n" + Constants.BADGED_CLIENT +"\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN ,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR') ||  isAuthenticated() && #userId == principal.id")
    @GetMapping("user/{userId}")
    public ResponseEntity<PageResponse> getOrdersByUserId(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "MAS_RECIENTES") OrderDate orderDate) {

        if(isInvalidPageRequest(page, size))
            throw new GenericException("BAD PAGEABLE", "The amount of elements cannot be more than 50, and page index must not be less than zero", HttpStatus.BAD_REQUEST);

        Pageable pageable;
        if(orderDate.equals(OrderDate.MAS_RECIENTES))
            pageable = PageRequest.of(page, size, Sort.by("dateCreated").ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());

        return ResponseEntity.ok(orderService.getOrdersByUserId(userId, pageable));
    }


    @Operation(
            tags = {"Ordenes"},
            operationId = "getOrdersByUserid",
            summary = "Listar todas las ordenes",
            description = "Lista todas las ordenes. A su vez es posible filtrarlas" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN ,
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<PageResponse> getAllOrders(@RequestParam(value = "status", required = false) OrderStatus status,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "MAS_RECIENTES") OrderDate orderDate) {

        if(isInvalidPageRequest(page, size))
            throw new GenericException("BAD PAGEABLE", "The amount of elements cannot be more than 50, and page index must not be less than zero", HttpStatus.BAD_REQUEST);

        Pageable pageable;
        if(orderDate.equals(OrderDate.MAS_RECIENTES))
            pageable = PageRequest.of(page, size, Sort.by("dateCreated").ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());

        if(status != null)
            return  ResponseEntity.ok(orderService.getAllOrdersWithStatus(status, pageable));
        else
            return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }


    @Operation(
            tags = {"Ordenes"},
            operationId = "updateOrderStatus",
            summary = "Actualizar el estado de una orden",
            description = "Actualiza el estado de una orden especifica de manera manual" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN ,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orden actualizada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PatchMapping("{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "updateOrderPaymentStatus",
            summary = "Actualizar el estado de pago de una orden",
            description = "Actualiza el estado de pago de una orden especifica de manera manual" +
                    "\n\n" + Constants.BADGED_ADMIN ,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orden actualizada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PatchMapping("{orderId}/payment")
    public ResponseEntity<OrderDTO> updateOrderPaymentStatus(@PathVariable Long orderId, @RequestParam("status") PaymentStatus paymentStatus) {
        return ResponseEntity.ok(orderService.updateOrderPaymentStatus(orderId, paymentStatus));
    }

    @Operation(
            tags = {"Ordenes"},
            operationId = "deleteOrder",
            summary = "Eliminar una orden",
            description = "Elimina una orden\n" +
                    "\n\n" + Constants.BADGED_ADMIN ,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orden Eliminada", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @DeleteMapping("{orderId}")
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }
}
