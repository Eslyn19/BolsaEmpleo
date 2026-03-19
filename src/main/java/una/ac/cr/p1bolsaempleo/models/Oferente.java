package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "oferente")
public class Oferente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferente", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerAp;

    @Column(name = "nacionalidad", length = 80)
    private String nacionalidad;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "lugar_residencia", length = 200)
    private String lugarResidencia;

    @Column(name = "curriculum_pdf")
    private String curriculumPdf;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado idEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "aprobado_por")
    private Administrador aprobadoPor;

    @Column(name = "fecha_aprobacion")
    private Instant fechaAprobacion;

    @Transient
    public String getCorreo() {
        return idUsuario != null ? idUsuario.getCorreo() : null;
    }

    @Transient
    public void setCorreo(String correo) {
        if (idUsuario != null) {
            idUsuario.setCorreo(correo);
        }
    }

    @Transient
    public String getClaveHash() {
        return idUsuario != null ? idUsuario.getClaveHash() : null;
    }

    @Transient
    public void setClaveHash(String claveHash) {
        if (idUsuario != null) {
            idUsuario.setClaveHash(claveHash);
        }
    }
}