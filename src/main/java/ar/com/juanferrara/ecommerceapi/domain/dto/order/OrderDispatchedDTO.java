package ar.com.juanferrara.ecommerceapi.domain.dto.order;

import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDispatchedDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "")
    private OrderDTO order;

    private EmployeeDTO employee;

    @Schema(example = "01/01/2024")
    private Date dateDispatched;

    @Schema(example = "ANDREANI")
    private String shippingCompany;

    @Schema(example = "ZZEEE221331EEDDDFFF")
    private String trackingCode;
}
