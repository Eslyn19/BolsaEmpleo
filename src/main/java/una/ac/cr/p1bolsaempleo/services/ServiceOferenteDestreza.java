package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.OferenteHabilidadRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.models.OferentehabilidadId;

import java.util.List;

@Service
public class ServiceOferenteDestreza {
    private final OferenteHabilidadRepository oferenteHabilidades;

    public ServiceOferenteDestreza(OferenteHabilidadRepository oferenteHabilidades) {
        this.oferenteHabilidades = oferenteHabilidades;
    }

    public List<Oferentehabilidad> obtenerTodasDestrezas() {
        return oferenteHabilidades.findAll();
    }

    public List<Oferentehabilidad> obtenerDestrezasPorOferente(Oferente oferente) {
        if (oferente == null || oferente.getIdUsuario() == null) {
            return List.of();
        }
        return oferenteHabilidades.findByIdUsuario_IdUsuario(oferente.getIdUsuario());
    }

    public Oferentehabilidad agregarDestreza(Oferentehabilidad destreza) {
        return oferenteHabilidades.save(destreza);
    }

    public Oferentehabilidad actualizarNivel(Oferentehabilidad destreza, int nuevoNivel) {
        destreza.setNivel(nuevoNivel);
        return oferenteHabilidades.save(destreza);
    }

    public boolean tieneDestreza(Oferente oferente, Integer habilidadId) {
        if (oferente == null || oferente.getIdUsuario() == null || habilidadId == null) {
            return false;
        }
        return oferenteHabilidades.existsByIdUsuario_IdUsuarioAndIdCaracteristica_Id(oferente.getIdUsuario(), habilidadId);
    }

    public Oferentehabilidad crearRelacion(Oferente oferente, Caracteristica caracteristica, int nivel) {
        Oferentehabilidad rel = new Oferentehabilidad();
        OferentehabilidadId id = new OferentehabilidadId();
        id.setIdUsuario(oferente.getIdUsuario());
        id.setIdCaracteristica(caracteristica.getId());
        rel.setId(id);
        rel.setIdUsuario(oferente);
        rel.setIdCaracteristica(caracteristica);
        rel.setNivel(nivel);
        return rel;
    }
}

