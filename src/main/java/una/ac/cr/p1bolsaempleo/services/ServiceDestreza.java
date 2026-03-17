package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.p1bolsaempleo.data.DestrezaRepository;
import una.ac.cr.p1bolsaempleo.models.Destreza;

import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service
public class ServiceDestreza {
    @Autowired
    private DestrezaRepository destrezas;

        public List<Destreza> DestrezasAll(){
            return destrezas.findAll();
        }

        public Destreza DestrezasGet(Integer id){
            return destrezas.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Destreza no encontrada"));
        }
        public void borrar(Integer id){
            destrezas.deleteById(id);
        }
    public List<Destreza> obtenerRuta(Integer id) {
        // Recorrer hacia arriba con padre hasta llegar a raíz
        List<Destreza> ruta = new ArrayList<>();
        Destreza actual = buscarPorId(id);
        while (actual != null) {
            ruta.add(0, actual); // insertar al inicio
            actual = actual.getPadre();
        }
        return ruta;
    }

    public List<Destreza> obtenerSubDestrezas(Integer padreId) {
        return destrezas.findByPadreId(padreId);
    }

    public void agregarOActualizarDestreza(Destreza destreza) {
        if (destreza.getId() != null && destrezas.existsById(destreza.getId())) {
            Destreza existente = destrezas.findById(destreza.getId()).orElse(null);
            if (existente != null) { // actualizar campos
                existente.setNombre(destreza.getNombre());
                existente.setDescripcion(destreza.getDescripcion());
                existente.setActiva(destreza.getActiva());
                existente.setPadre(destreza.getPadre());
                destrezas.save(existente);
            }
        } else {
            destrezas.save(destreza);
        }
    }
    public Destreza buscarPorId(Integer id) {
        return destrezas.findById(id).orElse(null);
    }
}
