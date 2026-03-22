package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    boolean existsByIdUsuario(String idUsuario);
}
