package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleados")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Employee {

    @Id
    @Column(name = "dni")
    private Long dni;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "idusuarios")
    private User user;

    @Column(name = "nombres")
    private String names;

    @Column(name = "apellido")
    private String lastName;
}
