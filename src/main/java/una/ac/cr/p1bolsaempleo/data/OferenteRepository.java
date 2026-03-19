package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.EstadoAprobacion;

import java.util.List;
import java.util.Optional;

public interface OferenteRepository extends JpaRepository<Oferente, Integer> {
    Optional<Oferente> findByIdUsuario_Correo(String correo);
    Optional<Oferente> findByIdentificacion(String identificacion);

    List<Oferente> findByIdEstado_Nombre(EstadoAprobacion estado);
}
