package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    @EntityGraph(attributePaths = {"caracteristicas", "oferenteAsignado"})
    List<Puesto> findByIdUsuario_IdUsuarioOrderByIdDesc(String idUsuario);

    Optional<Puesto> findByIdAndIdUsuario_IdUsuario(Integer id, String idUsuario);

    @EntityGraph(attributePaths = {"caracteristicas", "idUsuario"})
    @Query("SELECT p FROM Puesto p WHERE p.id = :id AND p.idUsuario.idUsuario = :emp")
    Optional<Puesto> findByIdAndEmpresaWithCaracteristicas(@Param("id") Integer id, @Param("emp") String emp);

    /** Puestos activos sin candidato aceptado (visibles en “Buscar candidatos”). */
    @EntityGraph(attributePaths = {"caracteristicas"})
    @Query("""
            SELECT p FROM Puesto p
            WHERE p.idUsuario.idUsuario = :idUsuario
              AND p.activo = 1
              AND p.oferenteAsignado IS NULL
            ORDER BY p.id DESC
            """)
    List<Puesto> findAbiertosSinAsignarPorEmpresa(@Param("idUsuario") String idUsuario);

    /** Puestos públicos, activos y sin candidato asignado; relevancia: mayor salario, luego más reciente. */
    @EntityGraph(attributePaths = {"caracteristicas", "idUsuario"})
    @Query("""
            SELECT p FROM Puesto p
            WHERE p.acceso = 1 AND p.activo = 1 AND p.oferenteAsignado IS NULL
            ORDER BY p.salario DESC, p.id DESC
            """)
    List<Puesto> findPublicosParaInicio(Pageable pageable);

    /** Para asociar características: carga la colección (tabla puesto_caracteristica). */
    @EntityGraph(attributePaths = {"caracteristicas"})
    @Query("SELECT p FROM Puesto p WHERE p.id = :id")
    Optional<Puesto> findWithCaracteristicasById(@Param("id") Integer id);
}
