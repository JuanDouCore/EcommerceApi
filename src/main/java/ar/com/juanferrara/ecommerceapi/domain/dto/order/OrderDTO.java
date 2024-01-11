package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.address.AddressDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.OrderItem;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long clientUserId;

    @Schema(example = "200000")
    private BigDecimal totalCost;

    @Schema(example = "PENDIENTE_PAGO")
    private OrderStatus status;

    @Schema(example = "PENDIENTE")
    private PaymentStatus paymentStatus;

    private AddressDTO shippingAddress;

    private Set<OrderItemDTO> items;

    @Schema(example = "01/01/2024")
    private Date dateCreated;

    @Schema(example = "01/02/2024")
    private Date lastUpdate;

    @Schema(example = "link mercadopago for pay")
    private String preferenceIdPaymentMPago;

}
