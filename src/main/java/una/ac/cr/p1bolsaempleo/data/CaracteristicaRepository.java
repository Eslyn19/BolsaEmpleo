package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;

import java.util.List;
import java.util.Optional;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {
    List<Caracteristica> findByIdPadre_Id(Integer idPadre);
    Optional<Caracteristica> findByNombreAndIdPadreIsNull(String nombre);
    Optional<Caracteristica> findByNombreAndIdPadre_Id(String nombre, Integer idPadre);
}

