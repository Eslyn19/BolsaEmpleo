package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;

import java.util.List;
import java.util.Optional;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

    List<Caracteristica> findByIdPadreIsNullOrderByNombreAsc();

    List<Caracteristica> findByIdPadre_IdOrderByNombreAsc(Integer idPadre);

    Optional<Caracteristica> findByNombreIgnoreCaseAndIdPadreIsNull(String nombre);

    @Query("SELECT c FROM Caracteristica c LEFT JOIN FETCH c.idPadre WHERE c.id = :id")
    Optional<Caracteristica> findByIdFetchPadre(@Param("id") Integer id);

    @Query("SELECT c FROM Caracteristica c WHERE c.activo = 1 OR c.activo IS NULL ORDER BY c.nombre ASC")
    List<Caracteristica> findActivasParaSeleccionPuesto();
}
