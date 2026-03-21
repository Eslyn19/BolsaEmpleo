package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Usuario;

import java.util.Optional;
public interface LoginRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByIdUsuario(String usuario);
}
