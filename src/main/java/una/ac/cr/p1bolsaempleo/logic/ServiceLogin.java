package una.ac.cr.p1bolsaempleo.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.LoginRepository;

@Service
public class ServiceLogin {

    @Autowired
    private LoginRepository loginRepository;

    public Login autenticar(String usuario, String clave) {
        Login login = loginRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado. Regístrese primero."));

        if (!login.getClave().equals(clave)) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return login; // autenticación exitosa
    }
}