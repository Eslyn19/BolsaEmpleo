package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.p1bolsaempleo.data.OferenteRepository;
import una.ac.cr.p1bolsaempleo.models.Oferente;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceOferente {
    @Autowired
    private OferenteRepository oferentes;

    public List<Oferente> oferentesAll(){
        return oferentes.findAll();
    }
    public Oferente oferentesGet(String id){
        return oferentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
    }
    public void borrar(String id){
        oferentes.deleteById(id);
    }
    public void oferentesAdd(Oferente oferente){
        if(oferentes.existsById(oferente.getId())){
            Oferente existente = oferentes.findById(oferente.getId()).orElse(null);
            if(existente != null){
                // Comparamos campo por campo y actualizamos si hay cambios
                if(!oferente.getNombre().equals(existente.getNombre())){
                    existente.setNombre(oferente.getNombre());
                }
                if(!oferente.getPrimerAp().equals(existente.getPrimerAp())){
                    existente.setPrimerAp(oferente.getPrimerAp());
                }
                if(oferente.getNacionalidad() != existente.getNacionalidad()){
                    existente.setNacionalidad(oferente.getNacionalidad());
                }
                if(!oferente.getTelefono().equals(existente.getTelefono())){
                    existente.setTelefono(oferente.getTelefono());
                }
                if(!oferente.getCorreo().equals(existente.getCorreo())){
                    existente.setCorreo(oferente.getCorreo());
                }
                if(!oferente.getLugarResidencia().equals(existente.getLugarResidencia())){
                    existente.setLugarResidencia(oferente.getLugarResidencia());
                }
                // Guardamos el objeto actualizado
                oferentes.save(existente);
            }
        } else {
            // Si no existe, lo guardamos normal
            oferentes.save(oferente);
        }
    }
}
