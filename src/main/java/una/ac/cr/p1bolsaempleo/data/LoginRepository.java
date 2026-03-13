package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.logic.Login;
import java.util.Optional;
public interface LoginRepository extends JpaRepository<Login,String> {
    Optional<Login> findByUsuario(String usuario);
}
