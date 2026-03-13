package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.Entities.Administrador;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    Optional<Administrador> login(String identificacion, String clave);
}
