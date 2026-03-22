package una.ac.cr.p1bolsaempleo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.EstadoRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.models.Estado;
import una.ac.cr.p1bolsaempleo.models.Usuario;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaService(EmpresaRepository empresaRepository,
                          UsuarioRepository usuarioRepository,
                          EstadoRepository estadoRepository,
                          PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registrar(String correo, String nombre, String ubicacion, String telefono,
                            String descripcion, String clave) {
        if (usuarioRepository.existsByIdUsuario(correo)) {
            return "El correo ya está registrado";
        }
        Estado estadoPendiente = estadoRepository.findFirstByNombreOrderByIdAsc("PENDIENTE")
                .orElseThrow(() -> new IllegalStateException("Estado PENDIENTE no encontrado"));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(correo);
        usuario.setClave(passwordEncoder.encode(clave));
        usuario.setRol("EMPRESA");
        usuarioRepository.save(usuario);
        Empresa empresa = new Empresa();
        empresa.setIdUsuario(correo);
        empresa.setCorreo(correo);
        empresa.setNombre(nombre);
        empresa.setUbicacion(ubicacion);
        empresa.setTelefono(telefono);
        empresa.setDescripcion(descripcion);
        empresa.setTipo(null);
        empresa.setEstado(estadoPendiente);
        empresaRepository.save(empresa);
        return null;
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> login(String correo, String clave) {
        String id = correo != null ? correo.trim() : "";
        if (id.isEmpty()) {
            return Optional.empty();
        }
        return usuarioRepository.findById(id)
                .filter(u -> "EMPRESA".equalsIgnoreCase(trimRol(u.getRol())))
                .filter(u -> matchesClave(clave, u.getClave()))
                .flatMap(u -> empresaRepository.findByIdUsuarioWithEstado(id))
                .filter(e -> "ACEPTADO".equalsIgnoreCase(e.getEstado().getNombre()));
    }

    private static String trimRol(String rol) {
        return rol != null ? rol.trim() : "";
    }

    private boolean matchesClave(String raw, String stored) {
        if (stored == null) {
            return false;
        }
        if (stored.startsWith("$2")) {
            return passwordEncoder.matches(raw, stored);
        }
        return stored.equals(raw);
    }

    public List<Empresa> listarPendientes() {
        return empresaRepository.findByEstadoPendiente();
    }

    @Transactional
    public void aprobar(String idUsuario, String tipo) {
        Empresa empresa = empresaRepository.findById(idUsuario).orElseThrow();
        empresa.setTipo(tipo);
        Estado aceptado = estadoRepository.findFirstByNombreOrderByIdAsc("ACEPTADO").orElseThrow();
        empresa.setEstado(aceptado);
        empresaRepository.save(empresa);
    }

    @Transactional
    public void rechazar(String idUsuario) {
        Empresa empresa = empresaRepository.findById(idUsuario).orElseThrow();
        Estado rechazado = estadoRepository.findFirstByNombreOrderByIdAsc("RECHAZADO").orElseThrow();
        empresa.setEstado(rechazado);
        empresaRepository.save(empresa);
    }
}
