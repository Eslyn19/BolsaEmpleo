package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.PuestoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EmpresaController {

    private final EmpresaService empresaService;
    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;
    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaService empresaService, PuestoService puestoService,
                             CaracteristicaService caracteristicaService,
                             EmpresaRepository empresaRepository) {
        this.empresaService = empresaService;
        this.puestoService = puestoService;
        this.caracteristicaService = caracteristicaService;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping("/api/empresa/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        if (!body.get("clave").equals(body.get("claveConfirm"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Las contraseñas no coinciden"));
        }

        String error = empresaService.registrar(
                body.get("correo"), body.get("nombre"), body.get("ubicacion"),
                body.get("telefono"), body.get("descripcion"), body.get("clave"));
        
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Registro exitoso. Espere aprobación del administrador."));
    }

    @GetMapping("/api/empresa/dashboard")
    public ResponseEntity<?> dashboard(Authentication auth) {
        return empresaRepository.findById(auth.getName())
                .map(e -> ResponseEntity.ok((Object) Map.of(
                    "id", e.getIdUsuario(),
                    "nombre", e.getNombre(),
                    "correo", e.getCorreo(),
                    "ubicacion", e.getUbicacion(),
                    "telefono", e.getTelefono()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/empresa/puestos")
    public ResponseEntity<?> misPuestos(Authentication auth) {
        return ResponseEntity.ok(puestoService.listarPorEmpresa(auth.getName()));
    }

    @GetMapping("/api/empresa/caracteristicas")
    public ResponseEntity<?> caracteristicasDisponibles() {
        return ResponseEntity.ok(caracteristicaService.listarActivasParaSeleccionPuesto());
    }

    @PostMapping("/api/empresa/puestos")
    public ResponseEntity<?> crearPuesto(Authentication auth, @RequestBody Map<String, Object> body) {
        try {
            String descripcion = (String) body.get("descripcion");
            Double salario = body.get("salario") instanceof Number n ? n.doubleValue() : null;
            List<Integer> ids = parseIds(body.get("idCaracteristica"));
            
            puestoService.crear(auth.getName(), descripcion, salario, ids);
            return ResponseEntity.ok(Map.of("mensaje", "Puesto publicado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/empresa/puestos/{id}/desactivar")
    public ResponseEntity<?> desactivar(Authentication auth, @PathVariable Integer id) {
        puestoService.desactivar(auth.getName(), id);
        return ResponseEntity.ok(Map.of("mensaje", "Puesto desactivado"));
    }

    @PostMapping("/api/empresa/puestos/{id}/activar")
    public ResponseEntity<?> activar(Authentication auth, @PathVariable Integer id) {
        puestoService.activar(auth.getName(), id);
        return ResponseEntity.ok(Map.of("mensaje", "Puesto activado"));
    }

    @PostMapping("/api/empresa/puestos/{id}/acceso-publico")
    public ResponseEntity<?> accesoPublico(Authentication auth, @PathVariable Integer id) {
        puestoService.marcarAccesoPublico(auth.getName(), id);
        return ResponseEntity.ok(Map.of("mensaje", "Puesto marcado como público"));
    }

    @PostMapping("/api/empresa/puestos/{id}/acceso-privado")
    public ResponseEntity<?> accesoPrivado(Authentication auth, @PathVariable Integer id) {
        puestoService.marcarAccesoPrivado(auth.getName(), id);
        return ResponseEntity.ok(Map.of("mensaje", "Puesto marcado como privado"));
    }

    @GetMapping("/api/empresa/puestos/{id}/candidatos")
    public ResponseEntity<?> candidatos(Authentication auth, @PathVariable Integer id) {
        return puestoService.obtenerPuestoEmpresaParaCandidatos(id, auth.getName())
                .filter(p -> p.getOferenteAsignado() == null
                        && p.getActivo() != null && p.getActivo() == 1)
                .map(p -> ResponseEntity.ok((Object) puestoService.listarCandidatosCompatibles(p)))
                .orElseGet(() -> ResponseEntity.badRequest().body("Puesto no disponible para candidatos"));
    }

    @PostMapping("/api/empresa/puestos/{id}/asignar-candidato")
    public ResponseEntity<?> asignarCandidato(Authentication auth, @PathVariable Integer id,
                                              @RequestBody Map<String, String> body) {
        try {
            puestoService.asignarOferenteYCerrarPuesto(auth.getName(), id, body.get("idOferente").trim());
            return ResponseEntity.ok(Map.of("mensaje", "Candidato asignado. Puesto cerrado."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/empresa/buscar-puestos")
    public ResponseEntity<?> buscarPuestos(Authentication auth) {
        return ResponseEntity.ok(puestoService.listarAbiertosParaBuscarCandidatos(auth.getName()));
    }

    @SuppressWarnings("unchecked")
    private List<Integer> parseIds(Object raw) {
        if (raw == null) return null;
        List<Integer> ids = new ArrayList<>();
        if (raw instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Number n) ids.add(n.intValue());
                else if (item instanceof String s && !s.isBlank()) {
                    try { ids.add(Integer.parseInt(s.trim())); } catch (NumberFormatException ignored) {}
                }
            }
        }
        return ids.isEmpty() ? null : ids;
    }
}
