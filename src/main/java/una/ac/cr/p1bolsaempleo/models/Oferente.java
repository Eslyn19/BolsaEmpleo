package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oferente")
public class Oferente {
    @Id
    @Column(name = "idUsuario", nullable = false, length = 9)
    private String idUsuario;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "nacionalidad", nullable = false, length = 40)
    private String nacionalidad;

    @Column(name = "telefono", nullable = false, length = 11)
    private String telefono;

    @Column(name = "correo", nullable = false, length = 50)
    private String correo;

    @Column(name = "residencia", nullable = false, length = 50)
    private String residencia;

    @Column(name = "rutaCV", length = 200)
    private String rutaCV;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado", nullable = false)
    private Estado estado;


}