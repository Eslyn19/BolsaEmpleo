package una.ac.cr.p1bolsaempleo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.AdministradorRepository;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.EstadoRepository;
import una.ac.cr.p1bolsaempleo.data.UsuarioRepository;
import una.ac.cr.p1bolsaempleo.models.*;

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
        if (usuarios.findById(correo).isPresent()) {
            throw new IllegalArgumentException("Identificador/Correo ya registrado.");
        }

        Usuario u = new Usuario();
        u.setIdUsuario(correo);
        u.setClave(password);
        u.setRol(Rol.EMPRESA.name());
        u = usuarios.save(u);

        Empresa e = new Empresa();
        e.setUsuario(u);
        e.setNombre(nombreEmpresa);
        e.setUbicacion(localizacion);
        e.setTelefono(telefono);
        e.setDescripcion(descripcion);
        e.setTipo(EstadoAprobacion.PENDIENTE.name());
        return empresas.save(e);
    }

    public List<Empresa> listarPendientes() {
        return empresas.findByTipoIgnoreCase(EstadoAprobacion.PENDIENTE.name());
    }

    @Transactional
    public void cambiarEstado(String empresaId, EstadoAprobacion nuevoEstado, String adminId) {
        Empresa e = empresas.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        e.setTipo(nuevoEstado.name());

        empresas.save(e);
    }
}

