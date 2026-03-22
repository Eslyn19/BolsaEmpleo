package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Administrador;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, String> {

    @Query("SELECT a FROM Administrador a JOIN FETCH a.usuario WHERE a.idUsuario = :id")
    Optional<Administrador> findByIdWithUsuario(@Param("id") String id);
}
