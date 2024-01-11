package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direcciones")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Address {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "iddirecciones")
    private Long id;

    @Column(name = "calle")
    private String street;

    @Column(name = "ciudad")
    private String city;

    @Column(name = "provincia")
    private String province;

    @Column(name = "codigo_postal")
    private String zipCode;
}
