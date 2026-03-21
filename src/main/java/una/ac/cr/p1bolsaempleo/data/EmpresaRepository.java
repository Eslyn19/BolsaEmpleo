package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import una.ac.cr.p1bolsaempleo.models.Empresa;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    List<Empresa> findByTipoIgnoreCase(String tipo);
}

