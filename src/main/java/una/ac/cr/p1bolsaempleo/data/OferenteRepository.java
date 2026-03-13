package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.logic.Oferente;

import java.util.Optional;

public interface OferenteRepository  extends JpaRepository<Oferente,String> {
    Optional<Oferente> findByCorreo(String correo);
}
