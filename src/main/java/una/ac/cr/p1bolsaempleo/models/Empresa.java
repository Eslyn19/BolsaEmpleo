package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @Column(name = "idUsuario", nullable = false, length = 9)
    private String idUsuario;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "ubicacion", nullable = false, length = 45)
    private String ubicacion;

    @Column(name = "telefono", nullable = false, length = 11)
    private String telefono;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    /**
     * script.sql: ENUM('PUBLICO','PRIVADO'). Si en tu BD usas PENDIENTE/APROBADO para aprobaciones,
     * la columna debe ser VARCHAR o un ENUM ampliado; sin columnDefinition para no forzar DDL.
     */
    @Column(name = "tipo", length = 30)
    private String tipo;


}