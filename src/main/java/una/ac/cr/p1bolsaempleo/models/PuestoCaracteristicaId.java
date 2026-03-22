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
public class PuestoCaracteristicaId implements Serializable {
    private static final long serialVersionUID = 4130324307266681582L;
    @Column(name = "idPuesto", nullable = false)
    private Integer idPuesto;

    @Column(name = "idCaracteristica", nullable = false)
    private Integer idCaracteristica;


}