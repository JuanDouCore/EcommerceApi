package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderItem {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "iditem_pedido")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName = "idproductos")
    private Product product;

    @Column(name = "cantidad")
    private int quantity;

    @Column(name = "costo_total", precision = 10, scale = 2) // precision = 10, scale = 2 => 10 digitos, 2 decimales.
    private BigDecimal totalCost;
}
