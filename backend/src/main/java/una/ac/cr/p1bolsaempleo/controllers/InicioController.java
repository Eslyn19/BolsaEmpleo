package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Puesto;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.PuestoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class InicioController {

    private final CaracteristicaService caracteristicaService;
    private final PuestoService puestoService;

    public InicioController(PuestoService puestoService, CaracteristicaService caracteristicaService) {
        this.puestoService = puestoService;
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping("/api/inicio")
    public ResponseEntity<?> inicio() {
        return ResponseEntity.ok(puestoService.listarCincoPublicosParaInicio());
    }

    @GetMapping("/api/buscar")
    public ResponseEntity<?> buscar(@RequestParam(required = false) List<Integer> caracteristicas) {
        Map<Caracteristica, List<Caracteristica>> arbolMap = caracteristicaService.arbolHojasActivas();
        List<Map<String, Object>> arbol = new ArrayList<>();
        
        arbolMap.forEach((padre, hijos) -> {
            String nombrePadre = padre != null ? padre.getNombre() : "Sin categoría";
            Integer idPadre = padre != null ? padre.getId() : 0;
            arbol.add(Map.of(
                    "padre", Map.of("id", idPadre, "nombre", nombrePadre),
                    "hijos", hijos
            ));
        });
        
        List<Puesto> puestos = puestoService.buscarPublicosPorCaracteristicas(caracteristicas);
        return ResponseEntity.ok(Map.of("arbol", arbol, "puestos", puestos));
    }
}
