package una.ac.cr.p1bolsaempleo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.models.OferentehabilidadId;

import java.util.List;

public interface OferenteHabilidadRepository extends JpaRepository<Oferentehabilidad, OferentehabilidadId> {

    @Query("""
        SELECT oh FROM Oferentehabilidad oh
        JOIN FETCH oh.idCaracteristica c
        WHERE oh.idUsuario.idUsuario = :idUsuario
    """)
    List<Oferentehabilidad> findByOferente(@Param("idUsuario") String idUsuario);

}
