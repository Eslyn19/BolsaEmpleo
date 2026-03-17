package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Destreza;
import una.ac.cr.p1bolsaempleo.models.Login;

import java.util.List;
import java.util.Optional;

public interface DestrezaRepository extends JpaRepository<Destreza, Integer> {
    List<Destreza> findByPadreId(Integer padreId);
}
