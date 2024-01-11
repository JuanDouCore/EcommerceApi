package ar.com.juanferrara.ecommerceapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "clientes")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Client {

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

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date birthDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "direcciones_clientes",
            joinColumns = @JoinColumn(name = "dni_cliente", referencedColumnName = "dni"),
            inverseJoinColumns = @JoinColumn(name = "id_direccion", referencedColumnName = "iddirecciones"))
    private Set<Address> addresses;
}
