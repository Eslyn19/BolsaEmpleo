package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.OferenteDestresaRepository;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.OferenteDestreza;


import java.util.List;

@Service
public class ServiceOferenteDestreza {

    private final OferenteDestresaRepository destrezaRepository;

    public ServiceOferenteDestreza(OferenteDestresaRepository destrezaRepository) {
        this.destrezaRepository = destrezaRepository;
    }

    public List<OferenteDestreza> obtenerTodasDestrezas() {
        return destrezaRepository.findAll();
    }

    public List<OferenteDestreza> obtenerDestrezasPorOferente(Oferente oferente) {
        return destrezaRepository.findByOferente(oferente);
    }

    // Agrega una nueva destreza con nivel
    public OferenteDestreza agregarDestreza(OferenteDestreza destreza) {
        return destrezaRepository.save(destreza);
    }

    // Actualiza el nivel de una destreza existente
    public OferenteDestreza actualizarNivel(OferenteDestreza destreza, int nuevoNivel) {
        destreza.setNivel(nuevoNivel);
        return destrezaRepository.save(destreza);
    }

    // Elimina una destreza
    public void eliminarDestreza(Integer id) {
        destrezaRepository.deleteById(id);
    }

    public boolean tieneDestreza(Oferente oferente, Integer habilidadId) {
        return destrezaRepository.findByOferente(oferente)
                .stream()
                .anyMatch(d -> d.getDestreza().getId().equals(habilidadId));
    }
}
