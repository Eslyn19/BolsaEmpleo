package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Estado;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    Optional<Estado> findFirstByNombreOrderByIdAsc(String nombre);
}
