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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
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

    public List<Oferente> oferentesAll(){
        return oferentes.findAll();
    }
    public Oferente oferentesGet(Integer id){
        return oferentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
    }
    public void borrar(Integer id){
        oferentes.deleteById(id);
    }
    public void oferentesAdd(Oferente oferente){
        if(oferentes.existsById(oferente.getId())){
            Oferente existente = oferentes.findById(oferente.getId()).orElse(null);
            if(existente != null){
                // Comparamos campo por campo y actualizamos si hay cambios
                if(!oferente.getNombre().equals(existente.getNombre())){
                    existente.setNombre(oferente.getNombre());
                }
                if(!oferente.getPrimerAp().equals(existente.getPrimerAp())){
                    existente.setPrimerAp(oferente.getPrimerAp());
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
                if(!oferente.getLugarResidencia().equals(existente.getLugarResidencia())){
                    existente.setLugarResidencia(oferente.getLugarResidencia());
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
        if (password == null || !password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (usuarios.findByCorreo(correo).isPresent()) {
            throw new IllegalArgumentException("Correo ya registrado.");
        }

        Estado estadoPendiente = estados.findByNombre(EstadoAprobacion.PENDIENTE)
                .orElseGet(() -> {
                    Estado e = new Estado();
                    e.setNombre(EstadoAprobacion.PENDIENTE);
                    return estados.save(e);
                });

        Usuario u = new Usuario();
        u.setCorreo(correo);
        u.setClaveHash(password);
        u.setTipo(Rol.OFERENTE);
        u.setActivo(true);
        u.setFechaRegistro(Instant.now());
        u = usuarios.save(u);

        Oferente o = new Oferente();
        o.setIdUsuario(u);
        o.setIdentificacion(identificacion);
        o.setNombre(nombre);
        o.setPrimerAp(primerAp);
        o.setNacionalidad(nacionalidad);
        o.setTelefono(telefono);
        o.setLugarResidencia(lugarResidencia);
        o.setCurriculumPdf(null);
        o.setIdEstado(estadoPendiente);
        return oferentes.save(o);
    }

    public List<Oferente> listarPendientes() {
        return oferentes.findByIdEstado_Nombre(EstadoAprobacion.PENDIENTE);
    }

    @Transactional(readOnly = true)
    public Optional<Oferente> loginOferente(String identificacion, String clave) {
        return autenticarOferente(identificacion, clave)
                .filter(o -> o.getIdEstado() != null && o.getIdEstado().getNombre() == EstadoAprobacion.APROBADO);
    }

    @Transactional(readOnly = true)
    public Optional<Oferente> autenticarOferente(String identificacion, String clave) {
        return oferentes.findByIdentificacion(identificacion)
                .filter(o -> o.getIdUsuario() != null && Boolean.TRUE.equals(o.getIdUsuario().getActivo()))
                .filter(o -> {
                    String stored = o.getIdUsuario().getClaveHash();
                    if (stored == null) return false;

                    // Si la clave está en BCrypt en BD
                    if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        return encoder.matches(clave, stored);
                    }

                    // Si la clave se guardó en texto plano (como actualmente registrarOferente)
                    return stored.equals(clave);
                });
    }

    @Transactional
    public void cambiarEstado(Integer oferenteId, EstadoAprobacion nuevoEstado, Integer adminId) {
        Oferente o = oferentes.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
        Estado estado = estados.findByNombre(nuevoEstado)
                .orElseThrow(() -> new IllegalStateException("Estado no existe: " + nuevoEstado));

        o.setIdEstado(estado);
        if (adminId != null) {
            Administrador admin = administradores.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado"));
            o.setAprobadoPor(admin);
            o.setFechaAprobacion(Instant.now());
        }
        oferentes.save(o);
    }
}
