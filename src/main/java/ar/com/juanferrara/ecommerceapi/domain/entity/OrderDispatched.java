package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "pedidos_despachados")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDispatched {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "idpedidos_despachados")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "idpedido")
    private Order order;

    @OneToOne
    @JoinColumn(name = "id_empleado_despachante", referencedColumnName = "dni")
    private Employee employee;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_despacho")
    private Date dateDispatched;

    @Column(name = "empresa_envio")
    private String shippingCompany;

    @Column(name = "codigo_seguimiento")
    private String trackingCode;
}
