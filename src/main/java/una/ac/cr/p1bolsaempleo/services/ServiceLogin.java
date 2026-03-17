package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.LoginRepository;
import una.ac.cr.p1bolsaempleo.models.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ServiceLogin {

    @Autowired
    private LoginRepository loginRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Login autenticar(String usuario, String clave) {
        Optional<Login> loginOpt = loginRepository.findByUsuario(usuario);

        if (loginOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Login login = loginOpt.get();

        // Verificar hash
        if (!passwordEncoder.matches(clave, login.getClave())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // Aquí podrías validar estado aprobado si lo deseas
        return login;
    }

    public void registrarUsuario(String usuario, String clavePlano, int tipo) {
        String hash = passwordEncoder.encode(clavePlano);
        Login nuevo = new Login(usuario, hash, tipo);
        loginRepository.save(nuevo);
    }

}