package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Oferente;

import java.util.List;
import java.util.Optional;

public interface OferenteRepository extends JpaRepository<Oferente, String> {

    @Query("""
        SELECT o FROM Oferente o
        WHERE o.estado.nombre = 'PENDIENTE'
    """)
    List<Oferente> findByEstadoPendiente();

    @Query("SELECT o FROM Oferente o JOIN FETCH o.estado WHERE o.idUsuario = :id")
    Optional<Oferente> findByIdWithEstado(@Param("id") String id);
}
