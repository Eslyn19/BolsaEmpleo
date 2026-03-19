package una.ac.cr.p1bolsaempleo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.Rol;
import una.ac.cr.p1bolsaempleo.models.Usuario;

import java.time.Instant;

@SpringBootApplication
public class P1BolsaEmpleoApplication {
    public static void main(String[] args) {
        SpringApplication.run(P1BolsaEmpleoApplication.class, args);
    }

    @Bean
    CommandLineRunner seedRootAdmin(AdministradorRepository administradorRepository, UsuarioRepository usuarioRepository) {
        return args -> {
            String identificacion = "root";
            if (administradorRepository.findByIdentificacion(identificacion).isPresent()) {
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setCorreo("root");
            usuario.setClaveHash("123");
            usuario.setTipo(Rol.ADMIN);
            usuario.setActivo(true);
            usuario.setFechaRegistro(Instant.now());
            usuario = usuarioRepository.save(usuario);

            Administrador admin = new Administrador();
            admin.setIdUsuario(usuario);
            admin.setIdentificacion(identificacion);
            admin.setNombre("root");
            administradorRepository.save(admin);
        };
    }
}
