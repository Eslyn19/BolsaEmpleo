package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    @EntityGraph(attributePaths = {"caracteristicas"})
    List<Puesto> findByIdUsuario_IdUsuarioOrderByIdDesc(String idUsuario);

    Optional<Puesto> findByIdAndIdUsuario_IdUsuario(Integer id, String idUsuario);

    /** Para asociar características: carga la colección (tabla puesto_caracteristica). */
    @EntityGraph(attributePaths = {"caracteristicas"})
    @Query("SELECT p FROM Puesto p WHERE p.id = :id")
    Optional<Puesto> findWithCaracteristicasById(@Param("id") Integer id);
}
