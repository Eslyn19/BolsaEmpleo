package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.models.OferentehabilidadId;

import java.util.List;
import java.util.Optional;

public interface OferenteHabilidadRepository extends JpaRepository<Oferentehabilidad, OferentehabilidadId> {
    List<Oferentehabilidad> findByIdUsuario_IdUsuario(String idUsuario);

    Optional<Oferentehabilidad> findByIdUsuario_IdUsuarioAndIdCaracteristica_Id(String idUsuario, Integer idCaracteristica);

    boolean existsByIdUsuario_IdUsuarioAndIdCaracteristica_Id(String idUsuario, Integer idCaracteristica);
}
