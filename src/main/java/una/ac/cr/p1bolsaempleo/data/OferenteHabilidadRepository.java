package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.OferenteHabilidad;
import una.ac.cr.p1bolsaempleo.models.OferenteHabilidadId;

import java.util.List;
import java.util.Optional;

public interface OferenteHabilidadRepository extends JpaRepository<OferenteHabilidad, OferenteHabilidadId> {
    List<OferenteHabilidad> findByIdOferente_Id(Integer idOferente);

    Optional<OferenteHabilidad> findByIdOferente_IdAndIdCaracteristica_Id(Integer idOferente, Integer idCaracteristica);

    boolean existsByIdOferente_IdAndIdCaracteristica_Id(Integer idOferente, Integer idCaracteristica);
}

