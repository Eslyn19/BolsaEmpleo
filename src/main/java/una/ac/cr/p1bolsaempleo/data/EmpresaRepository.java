package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import una.ac.cr.p1bolsaempleo.models.Empresa;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {

    @Query("SELECT e FROM Empresa e JOIN FETCH e.estado est WHERE est.nombre = 'PENDIENTE'")
    List<Empresa> findByEstadoPendiente();
}
