package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Product {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "idproductos")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "idcategorias_productos")
    private ProductCategory category;

    @Column(name = "url_imagen")
    private String imageUrl;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "total_vendidos")
    private Long totalSold;
}
