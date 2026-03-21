package una.ac.cr.p1bolsaempleo.services;

import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.models.Administrador;

import java.util.Optional;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    /**
     * Login admin: debe existir la identificación en BD y la clave guardada debe coincidir (texto plano, sin hash).
     */
    public Optional<Administrador> login(String id, String clave) {
        if (id == null || clave == null) {
            return Optional.empty();
        }
        String identificacion = id.trim();
        if (identificacion.isEmpty()) {
            return Optional.empty();
        }

        return administradorRepository.findByIdUsuario(identificacion)
                .filter(a -> a.getUsuario() != null && a.getUsuario().getClave() != null)
                .filter(a -> a.getUsuario().getClave().equals(clave.trim()));
    }
}
