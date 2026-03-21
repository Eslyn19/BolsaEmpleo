package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Oferente;

import java.util.List;
import java.util.Optional;

public interface OferenteRepository extends JpaRepository<Oferente, String> {
    Optional<Oferente> findByCorreo(String correo);

    Optional<Oferente> findByIdUsuario(String idUsuario);

    List<Oferente> findByEstado_Nombre(String estadoNombre);
}
