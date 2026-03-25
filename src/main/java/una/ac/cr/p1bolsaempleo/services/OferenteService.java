package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import una.ac.cr.p1bolsaempleo.data.*;
import una.ac.cr.p1bolsaempleo.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class OferenteService {
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    @Autowired
    private OferenteHabilidadRepository oferenteHabilidadRepository;
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

    public Oferente buscarPorId(String idUsuario) {
        return oferenteRepository.findById(idUsuario).orElse(null);
    }

    public String guardarCV(String idUsuario, MultipartFile archivo) throws IOException {

        Oferente oferente = oferenteRepository.findById(idUsuario)
                .orElseThrow();

        String carpeta = "uploads/cv/";
        File directorio = new File(carpeta);

        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        //  nombre original del archivo
        String nombreOriginal = archivo.getOriginalFilename();

        //  lo combinas con el usuario (para evitar conflictos)
        String nombreArchivo = idUsuario + "_" + nombreOriginal;

        Path ruta = Paths.get(carpeta + nombreArchivo);

        //  sobrescribe si ya existe
        Files.write(ruta, archivo.getBytes());

        // guardas ruta en BD
        oferente.setRutaCV("/" + carpeta + nombreArchivo);
        oferenteRepository.save(oferente);

        return oferente.getRutaCV();
    }
    @Transactional
    public void agregarHabilidad(String idUsuario, Integer idCaracteristica, int nivel) {

        Oferente oferente = oferenteRepository.findById(idUsuario)
                .orElseThrow();

        Caracteristica caracteristica = caracteristicaRepository.findById(idCaracteristica)
                .orElseThrow();

        OferentehabilidadId id = new OferentehabilidadId();
        id.setIdUsuario(idUsuario);
        id.setIdCaracteristica(idCaracteristica);

        Optional<Oferentehabilidad> existente = oferenteHabilidadRepository.findById(id);

        if (existente.isPresent()) {
            existente.get().setNivel(nivel);
            oferenteHabilidadRepository.save(existente.get());
            return;
        }

        Oferentehabilidad oh = new Oferentehabilidad();
        oh.setId(id);
        oh.setIdUsuario(oferente);
        oh.setIdCaracteristica(caracteristica);
        oh.setNivel(nivel);

        oferenteHabilidadRepository.save(oh);
    }

    @Transactional
    public String registrar(String identificacion, String nombre, String primerAp, String nacionalidad,
                            String telefono, String correo, String lugarResidencia, String clave) {
        identificacion = identificacion != null ? identificacion.trim() : "";
        if (identificacion.isEmpty()) {
            return "La identificación es obligatoria";
        }
        if (usuarioRepository.existsByIdUsuario(identificacion)) {
            return "La identificación ya está registrada";
        }
        Estado estado = estadoRepository.findFirstByNombreOrderByIdAsc("PENDIENTE")
                .orElseThrow(() -> new IllegalStateException("Estado PENDIENTE no encontrado"));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(identificacion);
        usuario.setClave(passwordEncoder.encode(clave));
        usuario.setRol("ROLE_OFERENTE");
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
        Estado aprobado = estadoRepository.findFirstByNombreOrderByIdAsc("APROBADO")
                .orElseThrow(() -> new IllegalStateException("Estado APROBADO no encontrado"));
        oferente.setEstado(aprobado);
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
