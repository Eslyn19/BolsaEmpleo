package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final AdministradorService administradorService;

    public AdminController(EmpresaService empresaService,
                           OferenteService oferenteService,
                           CaracteristicaService caracteristicaService,
                           AdministradorService administradorService) {
        this.empresaService = empresaService;
        this.oferenteService = oferenteService;
        this.caracteristicaService = caracteristicaService;
        this.administradorService = administradorService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return ResponseEntity.ok(Map.of(
            "empresasPendientes", empresaService.listarPendientes().size(),
            "oferentesPendientes", oferenteService.listarPendientes().size()
        ));
    }

    @GetMapping("/empresas")
    public ResponseEntity<?> empresas() {
        return ResponseEntity.ok(empresaService.listarPendientes());
    }

    @PostMapping("/empresas/{id}/aprobar")
    public ResponseEntity<?> aprobarEmpresa(@PathVariable String id, @RequestBody Map<String, String> body) {
        empresaService.aprobar(id, body.get("tipo"));
        return ResponseEntity.ok(Map.of("mensaje", "Empresa aprobada"));
    }

    @PostMapping("/empresas/{id}/rechazar")
    public ResponseEntity<?> rechazarEmpresa(@PathVariable String id) {
        empresaService.rechazar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Empresa rechazada"));
    }

    @GetMapping("/oferentes")
    public ResponseEntity<?> oferentes() {
        return ResponseEntity.ok(oferenteService.listarPendientes());
    }

    @PostMapping("/oferentes/{id}/aprobar")
    public ResponseEntity<?> aprobarOferente(@PathVariable String id) {
        oferenteService.aprobar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Oferente aprobado"));
    }

    @PostMapping("/oferentes/{id}/rechazar")
    public ResponseEntity<?> rechazarOferente(@PathVariable String id) {
        oferenteService.rechazar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Oferente rechazado"));
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<?> caracteristicas(@RequestParam(required = false) Integer parentId) {
        List<Caracteristica> items = caracteristicaService.listarItemsVista(parentId);
        List<Caracteristica> padresSelect = caracteristicaService.opcionesPadre(parentId);
        return ResponseEntity.ok(Map.of(
            "items", items,
            "padresSelect", padresSelect,
            "parentId", parentId != null ? parentId : 0
        ));
    }

    @PostMapping("/caracteristicas")
    public ResponseEntity<?> crearCaracteristica(@RequestBody Map<String, Object> body) {
        String nombre = (String) body.get("nombre");
        Integer idPadre = (Integer) body.get("idPadre");
        caracteristicaService.crear(nombre, idPadre);
        return ResponseEntity.ok(Map.of("mensaje", "Característica creada"));
    }

    @PostMapping("/caracteristicas/{id}/toggle-activo")
    public ResponseEntity<?> toggleActivo(@PathVariable Integer id) {
        caracteristicaService.alternarActivo(id);
        return ResponseEntity.ok(Map.of("mensaje", "Estado cambiado"));
    }
}
