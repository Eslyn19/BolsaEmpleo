package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class OferentehabilidadId implements Serializable {
    private static final long serialVersionUID = 6771875535579011161L;
    @Column(name = "idUsuario", nullable = false, length = 9)
    private String idUsuario;

    @Column(name = "idCaracteristica", nullable = false)
    private Integer idCaracteristica;


}