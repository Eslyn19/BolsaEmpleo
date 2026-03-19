package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;

import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service
public class ServiceDestreza {
    @Autowired
    private CaracteristicaRepository caracteristicas;

    public List<Caracteristica> DestrezasAll(){
        return caracteristicas.findAll();
    }

    public Caracteristica DestrezasGet(Integer id){
        return caracteristicas.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caracteristica no encontrada"));
    }
    public void borrar(Integer id){
        caracteristicas.deleteById(id);
    }
    public List<Caracteristica> obtenerRuta(Integer id) {
        // Recorrer hacia arriba con padre hasta llegar a raíz
        List<Caracteristica> ruta = new ArrayList<>();
        Caracteristica actual = buscarPorId(id);
        while (actual != null) {
            ruta.add(0, actual); // insertar al inicio
            actual = actual.getIdPadre();
        }
        return ruta;
    }

    public List<Caracteristica> obtenerSubDestrezas(Integer padreId) {
        return caracteristicas.findByIdPadre_Id(padreId);
    }

    public void agregarOActualizarDestreza(Caracteristica caracteristica) {
        if (caracteristica.getId() != null && caracteristicas.existsById(caracteristica.getId())) {
            Caracteristica existente = caracteristicas.findById(caracteristica.getId()).orElse(null);
            if (existente != null) {
                existente.setNombre(caracteristica.getNombre());
                existente.setIdPadre(caracteristica.getIdPadre());
                caracteristicas.save(existente);
            }
        } else {
            caracteristicas.save(caracteristica);
        }
    }
    public Caracteristica buscarPorId(Integer id) {
        return caracteristicas.findById(id).orElse(null);
    }
}