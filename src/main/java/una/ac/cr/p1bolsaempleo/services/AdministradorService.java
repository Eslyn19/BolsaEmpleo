package una.ac.cr.p1bolsaempleo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.models.Administrador;

import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Administrador> login(String identificacion, String clave) {
        return administradorRepository.findByIdWithUsuario(identificacion)
                .filter(admin -> matchesClave(clave, admin.getUsuario().getClave()));
    }

    private boolean matchesClave(String raw, String stored) {
        if (stored.startsWith("$2")) {
            return passwordEncoder.matches(raw, stored);
        }
        return stored.equals(raw);
    }
}
