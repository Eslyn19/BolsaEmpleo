package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.EstadoRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.models.Estado;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.EstadoAprobacion;
import una.ac.cr.p1bolsaempleo.models.Rol;
import una.ac.cr.p1bolsaempleo.models.Usuario;

import java.time.Instant;
import java.util.List;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository empresas;

    @Autowired
    private UsuarioRepository usuarios;

    @Autowired
    private EstadoRepository estados;

    @Autowired
    private AdministradorRepository administradores;

    @Transactional
    public Empresa registrarEmpresa(
            String nombreEmpresa,
            String localizacion,
            String correo,
            String telefono,
            String descripcion,
            String password,
            String passwordConfirm
    ) {
        if (password == null || !password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (telefono == null || !telefono.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 8 números.");
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
        u.setTipo(Rol.EMPRESA);
        u.setActivo(true);
        u.setFechaRegistro(Instant.now());
        u = usuarios.save(u);

        Empresa e = new Empresa();
        e.setIdUsuario(u);
        e.setNombre(nombreEmpresa);
        e.setLocalizacion(localizacion);
        e.setTelefono(telefono);
        e.setDescripcion(descripcion);
        e.setIdEstado(estadoPendiente);
        return empresas.save(e);
    }

    public List<Empresa> listarPendientes() {
        return empresas.findByIdEstado_Nombre(EstadoAprobacion.PENDIENTE);
    }

    @Transactional
    public void cambiarEstado(Integer empresaId, EstadoAprobacion nuevoEstado, Integer adminId) {
        Empresa e = empresas.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        Estado estado = estados.findByNombre(nuevoEstado)
                .orElseGet(() -> {
                    Estado created = new Estado();
                    created.setNombre(nuevoEstado);
                    return estados.save(created);
                });

        e.setIdEstado(estado);

        if (adminId != null) {
            Administrador admin = administradores.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado"));
            e.setAprobadoPor(admin);
            e.setFechaAprobacion(Instant.now());
        }

        empresas.save(e);
    }
}

