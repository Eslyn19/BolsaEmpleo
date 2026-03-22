package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import una.ac.cr.p1bolsaempleo.models.Oferente;

import java.util.List;

public interface OferenteRepository extends JpaRepository<Oferente, String> {

    @Query("SELECT o FROM Oferente o JOIN FETCH o.estado est WHERE est.nombre = 'PENDIENTE'")
    List<Oferente> findByEstadoPendiente();
}
