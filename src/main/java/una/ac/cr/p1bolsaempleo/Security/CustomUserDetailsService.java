package una.ac.cr.p1bolsaempleo.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Usuario;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final OferenteRepository oferenteRepository;
    private final EmpresaRepository empresaRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    OferenteRepository oferenteRepository,
                                    EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.oferenteRepository = oferenteRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identificacion) throws UsernameNotFoundException {
        String id = identificacion != null ? identificacion.trim() : "";
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rol = usuario.getRol() != null ? usuario.getRol().trim() : "";

        if ("ROLE_OFERENTE".equalsIgnoreCase(rol)) {
            Oferente oferente = oferenteRepository.findByIdWithEstado(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            String estado = oferente.getEstado() != null ? oferente.getEstado().getNombre() : "";
            if (!"APROBADO".equalsIgnoreCase(estado)) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
        } else if ("ROLE_EMPRESA".equalsIgnoreCase(rol)) {
            Empresa empresa = empresaRepository.findByIdUsuarioWithEstado(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            String estado = empresa.getEstado() != null ? empresa.getEstado().getNombre() : "";
            if (!"APROBADO".equalsIgnoreCase(estado)) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getIdUsuario())
                .password(usuario.getClave())
                .authorities(usuario.getRol())
                .build();
    }
}
