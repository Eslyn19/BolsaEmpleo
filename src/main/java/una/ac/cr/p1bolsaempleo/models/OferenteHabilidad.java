package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "oferente_habilidad")
public class OferenteHabilidad {
    @EmbeddedId
    private OferenteHabilidadId id;

    @MapsId("idOferente")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_oferente", nullable = false)
    private Oferente idOferente;

    @MapsId("idCaracteristica")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_caracteristica", nullable = false)
    private Caracteristica idCaracteristica;

    @Column(name = "nivel", nullable = false)
    private Byte nivel;

    @Transient
    public String getNombre() {
        return idCaracteristica != null ? idCaracteristica.getNombre() : null;
    }

    @Transient
    public Caracteristica getDestreza() {
        return idCaracteristica;
    }

    @Transient
    public Integer getIdDestreza() {
        return idCaracteristica != null ? idCaracteristica.getId() : null;
    }

}