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
public class OferenteHabilidadId implements Serializable {
    private static final long serialVersionUID = 6556863210186666459L;
    @Column(name = "id_oferente", nullable = false)
    private Integer idOferente;

    @Column(name = "id_caracteristica", nullable = false)
    private Integer idCaracteristica;


}