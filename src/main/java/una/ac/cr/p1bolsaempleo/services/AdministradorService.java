package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;

import java.util.Optional;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public Optional<Administrador> login(String id, String clave) {
        Optional<Administrador> adminOpt = administradorRepository.findByIdentificacion(id);

        if(adminOpt.isPresent()){
            Administrador admin = adminOpt.get();

            if(admin.getClaveHash().equals(clave)){
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }
}
