package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Usuario;

@Service
public class ServiceLogin {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(String usuario, String clave) {
        Usuario login = usuarioRepository.findById(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado. Regístrese primero."));

        if (!login.getClave().equals(clave)) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return login; // autenticación exitosa
    }
}