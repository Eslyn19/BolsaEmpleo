package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Administrador;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, String> {

    /** Trae también `usuario` (clave en texto plano, ej. 111). */
    @EntityGraph(attributePaths = "usuario")
    Optional<Administrador> findByIdUsuario(String idUsuario);
}
