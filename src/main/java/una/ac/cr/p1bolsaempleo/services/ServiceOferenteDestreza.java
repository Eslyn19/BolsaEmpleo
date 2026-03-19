package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.OferenteHabilidadRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.OferenteHabilidad;

import java.util.List;

@Service
public class ServiceOferenteDestreza {
    private final OferenteHabilidadRepository oferenteHabilidades;

    public ServiceOferenteDestreza(OferenteHabilidadRepository oferenteHabilidades) {
        this.oferenteHabilidades = oferenteHabilidades;
    }

    public List<OferenteHabilidad> obtenerTodasDestrezas() {
        return oferenteHabilidades.findAll();
    }

    public List<OferenteHabilidad> obtenerDestrezasPorOferente(Oferente oferente) {
        if (oferente == null || oferente.getId() == null) {
            return List.of();
        }
        return oferenteHabilidades.findByIdOferente_Id(oferente.getId());
    }

    public OferenteHabilidad agregarDestreza(OferenteHabilidad destreza) {
        return oferenteHabilidades.save(destreza);
    }

    public OferenteHabilidad actualizarNivel(OferenteHabilidad destreza, int nuevoNivel) {
        destreza.setNivel((byte) nuevoNivel);
        return oferenteHabilidades.save(destreza);
    }

    public boolean tieneDestreza(Oferente oferente, Integer habilidadId) {
        if (oferente == null || oferente.getId() == null || habilidadId == null) {
            return false;
        }
        return oferenteHabilidades.existsByIdOferente_IdAndIdCaracteristica_Id(oferente.getId(), habilidadId);
    }

    public OferenteHabilidad crearRelacion(Oferente oferente, Caracteristica caracteristica, int nivel) {
        OferenteHabilidad rel = new OferenteHabilidad();
        rel.setIdOferente(oferente);
        rel.setIdCaracteristica(caracteristica);
        rel.setNivel((byte) nivel);
        return rel;
    }
}

