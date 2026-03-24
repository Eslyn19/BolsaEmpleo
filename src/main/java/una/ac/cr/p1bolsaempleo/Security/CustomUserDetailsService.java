package una.ac.cr.p1bolsaempleo.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Usuario;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identificacion) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(identificacion)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getIdUsuario()) // o correo si usas correo
                .password(usuario.getClave()) // YA debe venir encriptada
                .authorities(usuario.getRol()) // ajusta según tu BD
                .build();
    }
}
