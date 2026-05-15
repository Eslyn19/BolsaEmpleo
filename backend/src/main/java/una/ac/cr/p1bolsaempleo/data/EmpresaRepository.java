package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {

    @Query("SELECT e FROM Empresa e JOIN FETCH e.estado est WHERE est.nombre = 'PENDIENTE'")
    List<Empresa> findByEstadoPendiente();

    @Query("SELECT e FROM Empresa e JOIN FETCH e.estado WHERE e.idUsuario = :id")
    Optional<Empresa> findByIdUsuarioWithEstado(@Param("id") String id);
}
