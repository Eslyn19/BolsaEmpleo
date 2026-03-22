package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "empresa")
public class Empresa {
    @Id
    @Column(name = "idUsuario", nullable = false, length = 50)
    private String idUsuario;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "ubicacion", nullable = false, length = 45)
    private String ubicacion;

    @Column(name = "telefono", nullable = false, length = 11)
    private String telefono;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "tipo", length = 30)
    private String tipo;

    @Column(name = "correo", nullable = false, length = 50)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado", nullable = false)
    private Estado estado;

    
}