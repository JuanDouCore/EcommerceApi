package ar.com.juanferrara.ecommerceapi.domain.entity;

import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import ar.com.juanferrara.ecommerceapi.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "pedidos")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Order {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "idpedido")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dni_cliente", referencedColumnName = "dni")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_direccion_envio", referencedColumnName = "iddirecciones")
    private Address shippingAddress;

    @Column(name = "costo_total", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago")
    private PaymentStatus paymentStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date dateCreated;

    @Temporal(TemporalType.DATE)
    @Column(name = "ultima_actualizacion")
    private Date lastUpdate;

    @Column(name = "order_reference_external")
    private String orderReferenceExternal;

    @Column(name = "preference_id_mpago")
    private String preferenceIdPaymentMPago;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "items_de_pedidos",
            joinColumns = @JoinColumn(name = "id_pedido", referencedColumnName = "idpedido"),
            inverseJoinColumns = @JoinColumn(name = "itempedido_id", referencedColumnName = "iditem_pedido"))
    private Set<OrderItem> items;
}
