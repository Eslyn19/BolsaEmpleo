package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteHabilidadRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oferente")
public class OferenteController {

    private final OferenteHabilidadRepository oferenteHabilidadRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;

    public OferenteController(OferenteService oferenteService,
                              OferenteHabilidadRepository oferenteHabilidadRepository,
                              CaracteristicaRepository caracteristicaRepository,
                              CaracteristicaService caracteristicaService) {
        this.oferenteService = oferenteService;
        this.oferenteHabilidadRepository = oferenteHabilidadRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.caracteristicaService = caracteristicaService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        if (!body.get("clave").equals(body.get("claveConfirm"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Las contraseñas no coinciden"));
        }
        String error = oferenteService.registrar(
                body.get("identificacion"), body.get("nombre"), body.get("primerAp"),
                body.get("nacionalidad"), body.get("telefono"), body.get("correo"),
                body.get("lugarResidencia"), body.get("clave"));
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Registro exitoso. Espere aprobación del administrador."));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(Authentication auth) {
        Oferente o = oferenteService.buscarPorId(auth.getName());
        if (o == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of(
                "id", o.getIdUsuario(),
                "nombre", o.getNombre(),
                "apellido", o.getApellido(),
                "correo", o.getCorreo(),
                "rutaCV", o.getRutaCV() != null ? o.getRutaCV() : ""
        ));
    }

    @GetMapping("/cv")
    public ResponseEntity<?> cv(Authentication auth) {
        Oferente o = oferenteService.buscarPorId(auth.getName());
        
        if (o == null){ 
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("rutaCV", o.getRutaCV() != null ? o.getRutaCV() : ""));
    }

    @PostMapping("/cv")
    public ResponseEntity<?> subirCV(@RequestParam("archivo") MultipartFile archivo, Authentication auth) {
        if (archivo.getContentType() == null || !archivo.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten archivos PDF"));
        }
        try {
            String ruta = oferenteService.guardarCV(auth.getName(), archivo);
            return ResponseEntity.ok(Map.of("rutaCV", ruta));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al subir el archivo"));
        }
    }

    @GetMapping("/habilidades")
    public ResponseEntity<?> habilidades(Authentication auth, @RequestParam(required = false) Integer actual) {
        String idUsuario = auth.getName();
        List<Oferentehabilidad> misHabilidades = oferenteHabilidadRepository.findByOferente(idUsuario);
        List<Caracteristica> subHabilidades;
        Caracteristica actualCat = null;
        
        if (actual == null) {
            subHabilidades = caracteristicaService.listarRaices();
        } else {
            actualCat = caracteristicaRepository.findById(actual).orElse(null);
            subHabilidades = caracteristicaService.listarHijos(actual);
        }
        
        List<Caracteristica> hojas = caracteristicaService.listarHojas(subHabilidades);
        List<Caracteristica> ruta = new ArrayList<>();
        Caracteristica temp = actualCat;
        
        while (temp != null) {
            ruta.add(0, temp);
            temp = temp.getIdPadre();
        }
        
        return ResponseEntity.ok(Map.of(
                "misHabilidades", misHabilidades,
                "subHabilidades", subHabilidades,
                "habilidadesFiltradas", hojas,
                "rutaActual", ruta
        ));
    }

    @PostMapping("/habilidades")
    public ResponseEntity<?> agregarHabilidad(Authentication auth, @RequestBody Map<String, Integer> body) {
        oferenteService.agregarHabilidad(auth.getName(), body.get("habilidadId"), body.get("nivel"));
        return ResponseEntity.ok(Map.of("mensaje", "Habilidad agregada"));
    }
}
