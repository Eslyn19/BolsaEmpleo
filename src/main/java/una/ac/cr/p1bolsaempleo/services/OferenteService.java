package una.ac.cr.p1bolsaempleo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.EstadoRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Estado;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Usuario;

import java.util.List;

@Service
public class OferenteService {

    private final OferenteRepository oferenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public OferenteService(OferenteRepository oferenteRepository,
                           UsuarioRepository usuarioRepository,
                           EstadoRepository estadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.oferenteRepository = oferenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registrar(String identificacion, String nombre, String primerAp, String nacionalidad,
                            String telefono, String correo, String lugarResidencia, String clave) {
        if (usuarioRepository.existsByIdUsuario(identificacion)) {
            return "La identificación ya está registrada";
        }
        Estado estado = estadoRepository.findFirstByNombreOrderByIdAsc("PENDIENTE")
                .orElseThrow(() -> new IllegalStateException("Estado PENDIENTE no encontrado"));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(identificacion);
        usuario.setClave(passwordEncoder.encode(clave));
        usuario.setRol("OFERENTE");
        usuarioRepository.save(usuario);
        Oferente oferente = new Oferente();
        oferente.setIdUsuario(identificacion);
        oferente.setNombre(nombre);
        oferente.setApellido(primerAp);
        oferente.setNacionalidad(nacionalidad);
        oferente.setTelefono(telefono != null ? telefono : "");
        oferente.setCorreo(correo);
        oferente.setResidencia(lugarResidencia);
        oferente.setRutaCV(null);
        oferente.setEstado(estado);
        oferenteRepository.save(oferente);
        return null;
    }

    public List<Oferente> listarPendientes() {
        return oferenteRepository.findByEstadoPendiente();
    }

    @Transactional
    public void aprobar(String idUsuario) {
        Oferente oferente = oferenteRepository.findById(idUsuario).orElseThrow();
        Estado aceptado = estadoRepository.findFirstByNombreOrderByIdAsc("ACEPTADO")
                .orElseThrow(() -> new IllegalStateException("Estado ACEPTADO no encontrado"));
        oferente.setEstado(aceptado);
        oferenteRepository.save(oferente);
    }

    @Transactional
    public void rechazar(String idUsuario) {
        Oferente oferente = oferenteRepository.findById(idUsuario).orElseThrow();
        Estado rechazado = estadoRepository.findFirstByNombreOrderByIdAsc("RECHAZADO")
                .orElseThrow(() -> new IllegalStateException("Estado RECHAZADO no encontrado"));
        oferente.setEstado(rechazado);
        oferenteRepository.save(oferente);
    }
}
