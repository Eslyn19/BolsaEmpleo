package una.ac.cr.p1bolsaempleo.data;


import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.OferenteDestreza;
import una.ac.cr.p1bolsaempleo.models.Oferente;

import java.util.List;

public interface OferenteDestresaRepository extends JpaRepository<OferenteDestreza, Integer> {
    List<OferenteDestreza> findByOferente(Oferente oferente);
}

