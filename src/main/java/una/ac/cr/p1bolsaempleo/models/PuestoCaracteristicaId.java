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
    private static final long serialVersionUID = -1467033379674615724L;
    @Column(name = "id_puesto", nullable = false)
    private Integer idPuesto;

    @Column(name = "id_caracteristica", nullable = false)
    private Integer idCaracteristica;


}