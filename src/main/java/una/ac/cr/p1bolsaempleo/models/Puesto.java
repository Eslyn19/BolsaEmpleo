package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "puesto")
public class Puesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPuesto", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false, referencedColumnName = "idUsuario")
    private Empresa idUsuario;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "salario", nullable = false)
    private Double salario;

    @Column(name = "activo")
    private Byte activo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "puesto_caracteristica",
            joinColumns = @JoinColumn(name = "idPuesto", referencedColumnName = "idPuesto"),
            inverseJoinColumns = @JoinColumn(name = "idCaracteristica", referencedColumnName = "id")
    )
    private Set<Caracteristica> caracteristicas = new LinkedHashSet<>();

}