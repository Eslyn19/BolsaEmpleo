package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.data.EstadoRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.Estado;
import una.ac.cr.p1bolsaempleo.models.EstadoAprobacion;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Rol;
import una.ac.cr.p1bolsaempleo.models.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceOferente {
    @Autowired
    private OferenteRepository oferentes;
    @Autowired
    private UsuarioRepository usuarios;
    @Autowired
    private EstadoRepository estados;
    @Autowired
    private AdministradorRepository administradores;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Oferente> oferentesAll(){
        return oferentes.findAll();
    }
    public Oferente oferentesGet(Integer id){
        return oferentes.findById(String.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
    }
    public void borrar(Integer id){
        oferentes.deleteById(String.valueOf(id));
    }
    public void oferentesAdd(Oferente oferente){
        if(oferentes.existsById(oferente.getIdUsuario())){
            Oferente existente = oferentes.findById(oferente.getIdUsuario()).orElse(null);
            if(existente != null){
                // Comparamos campo por campo y actualizamos si hay cambios
                if(!oferente.getNombre().equals(existente.getNombre())){
                    existente.setNombre(oferente.getNombre());
                }
                if(!oferente.getApellido().equals(existente.getApellido())){
                    existente.setApellido(oferente.getApellido());
                }
                if(oferente.getNacionalidad() != null && !oferente.getNacionalidad().equals(existente.getNacionalidad())){
                    existente.setNacionalidad(oferente.getNacionalidad());
                }
                if(oferente.getTelefono() != null && !oferente.getTelefono().equals(existente.getTelefono())){
                    existente.setTelefono(oferente.getTelefono());
                }
                if(oferente.getCorreo() != null && !oferente.getCorreo().equals(existente.getCorreo())){
                    existente.setCorreo(oferente.getCorreo());
                }
                if(!oferente.getResidencia().equals(existente.getResidencia())){
                    existente.setResidencia(oferente.getResidencia());
                }
                // Guardamos el objeto actualizado
                oferentes.save(existente);
            }
        } else {
            // Si no existe, lo guardamos normal
            oferentes.save(oferente);
        }
    }

    @Transactional
    public Oferente registrarOferente(
            String nombre,
            String primerAp,
            String nacionalidad,
            String identificacion,
            String telefono,
            String correo,
            String lugarResidencia,
            String password,
            String passwordConfirm
    ) {
        String identificacionNormalizada = identificacion == null ? "" : identificacion.trim();
        String correoNormalizado = correo == null ? "" : correo.trim().toLowerCase();

        if (password == null || !password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (identificacionNormalizada.isEmpty()) {
            throw new IllegalArgumentException("La identificación es obligatoria.");
        }
        if (correoNormalizado.isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (usuarios.findById(identificacionNormalizada).isPresent()) {
            throw new IllegalArgumentException("La identificación ya está registrada.");
        }
        if (oferentes.findByCorreo(correoNormalizado).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        Estado estadoPendiente = estados.findByNombre(EstadoAprobacion.PENDIENTE.name())
                .orElseGet(() -> {
                    Estado e = new Estado();
                    e.setNombre(EstadoAprobacion.PENDIENTE.name());
                    return estados.save(e);
                });

        Usuario u = new Usuario();
        u.setIdUsuario(identificacionNormalizada);
        u.setClave(passwordEncoder.encode(password));
        u.setRol(Rol.OFERENTE.name());
        u = usuarios.save(u);

        Oferente o = new Oferente();
        o.setIdUsuario(u.getIdUsuario());
        o.setUsuario(u);
        o.setNombre(nombre);
        o.setApellido(primerAp);
        o.setNacionalidad(nacionalidad);
        o.setTelefono(telefono != null && !telefono.isBlank() ? telefono.trim() : "00000000000");
        o.setCorreo(correoNormalizado);
        o.setResidencia(lugarResidencia);
        o.setRutaCV(null);
        o.setEstado(estadoPendiente);
        return oferentes.save(o);
    }

    public List<Oferente> listarPendientes() {
        return oferentes.findByEstado_Nombre(EstadoAprobacion.PENDIENTE.name());
    }

    @Transactional(readOnly = true)
    public Optional<Oferente> loginOferente(String identificacion, String clave) {
        return autenticarOferente(identificacion, clave)
                .filter(o -> o.getEstado() != null && EstadoAprobacion.APROBADO.name().equals(o.getEstado().getNombre()));
    }

    @Transactional(readOnly = true)
    public Optional<Oferente> autenticarOferente(String identificacion, String clave) {
        return oferentes.findByIdUsuario(identificacion)
                .filter(o -> o.getUsuario() != null)
                .filter(o -> {
                    String stored = o.getUsuario().getClave();
                    if (stored == null) return false;

                    // Si la clave está en BCrypt en BD
                    if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                        return passwordEncoder.matches(clave, stored);
                    }

                    // Datos antiguos en texto plano
                    return stored.equals(clave);
                });
    }

    @Transactional
    public void cambiarEstado(String oferenteId, EstadoAprobacion nuevoEstado, String adminId) {
        Oferente o = oferentes.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
        Estado estado = estados.findByNombre(nuevoEstado.name())
                .orElseThrow(() -> new IllegalStateException("Estado no existe: " + nuevoEstado));

        o.setEstado(estado);
        oferentes.save(o);
    }
}
