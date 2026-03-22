package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    public List<Caracteristica> listarRaices() {
        return caracteristicaRepository.findByIdPadreIsNullOrderByNombreAsc();
    }

    public List<Caracteristica> listarHijos(Integer idPadre) {
        return caracteristicaRepository.findByIdPadre_IdOrderByNombreAsc(idPadre);
    }

    public Optional<Caracteristica> obtenerConPadre(Integer id) {
        return caracteristicaRepository.findByIdFetchPadre(id);
    }

    public Optional<Integer> idRaizPasiva() {
        return caracteristicaRepository.findByNombreIgnoreCaseAndIdPadreIsNull("Raíz")
                .map(Caracteristica::getId);
    }

    public List<Caracteristica> listarItemsVista(Integer parentIdVista) {
        if (parentIdVista == null) {
            return idRaizPasiva()
                    .map(this::listarHijos)
                    .orElseGet(this::listarRaices);
        }
        return listarHijos(parentIdVista);
    }

    public List<Caracteristica> opcionesPadre(Integer parentIdVista) {
        if (parentIdVista == null) {
            return idRaizPasiva()
                    .map(this::listarHijos)
                    .orElseGet(this::listarRaices);
        }
        return obtenerConPadre(parentIdVista)
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    public boolean volverEsRaices(Caracteristica actual, Integer raizId) {
        if (actual.getIdPadre() == null) {
            return true;
        }
        return raizId != null && raizId.equals(actual.getIdPadre().getId());
    }

    @Transactional
    public void crear(String nombre, Integer idPadre) {
        Caracteristica padre = caracteristicaRepository.findById(idPadre)
                .orElseThrow(() -> new IllegalArgumentException("Padre no encontrado"));
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre.trim());
        c.setIdPadre(padre);
        c.setActivo((byte) 1);
        caracteristicaRepository.save(c);
    }
}
