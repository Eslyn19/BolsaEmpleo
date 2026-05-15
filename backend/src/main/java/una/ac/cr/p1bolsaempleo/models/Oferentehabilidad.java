package una.ac.cr.p1bolsaempleo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "oferentehabilidad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Oferentehabilidad {
    @EmbeddedId
    private OferentehabilidadId id;

    @MapsId("idUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Oferente idUsuario;

    @MapsId("idCaracteristica")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idCaracteristica", nullable = false)
    private Caracteristica idCaracteristica;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;


}