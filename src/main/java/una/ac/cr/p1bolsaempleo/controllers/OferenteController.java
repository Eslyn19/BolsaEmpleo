package una.ac.cr.p1bolsaempleo.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteHabilidadRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/oferente")
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
    @ModelAttribute
    public void agregarDatosUsuario(HttpSession session, Model model) {

        String id = (String) session.getAttribute("oferenteId");

        if (id != null) {
            Oferente oferente = oferenteService.buscarPorId(id);

            if (oferente != null) {
                model.addAttribute("usuarioNombre", oferente.getNombre());
                model.addAttribute("usuarioPrimerAp", oferente.getApellido());
            }
        }
    }


    @GetMapping("/userform")
    public String form() {
        return "FormOferente";
    }

    @GetMapping("/cv")
    public String vistaCV(HttpSession session, Model model) {

        String id = (String) session.getAttribute("oferenteId");

        if (id == null) {
            return "redirect:/login";
        }

        Oferente oferente = oferenteService.buscarPorId(id);
        model.addAttribute("rutaCV", oferente.getRutaCV());

        return "oferente/cv";
    }

    @PostMapping("/cv")
    public String subirCV(@RequestParam("archivo") MultipartFile archivo,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        String id = (String) session.getAttribute("oferenteId");

        if (id == null) {
            return "redirect:/login";
        }

        try {
            if (!archivo.getContentType().equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("error", "Solo se permiten archivos PDF");
                return "redirect:/oferente/cv";
            }

            String ruta = oferenteService.guardarCV(id, archivo);

            redirectAttributes.addFlashAttribute("cvSubido", true);
            redirectAttributes.addFlashAttribute("rutaCV", ruta);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo");
        }


        return "redirect:/oferente/cv";
    }
    @GetMapping("/entrar")
    public String entrar(@RequestParam Integer id) {
        return "redirect:/oferente/habilidades?actual=" + id;
    }
    @PostMapping("/agregar")
    public String agregar(@RequestParam Integer habilidadId,
                          @RequestParam int nivel,
                          HttpSession session,
                          @RequestParam(required = false) Integer actual) {

        String idUsuario = (String) session.getAttribute("oferenteId");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        oferenteService.agregarHabilidad(idUsuario, habilidadId, nivel);

        return "redirect:/oferente/habilidades?actual=" + actual;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String id = (String) session.getAttribute("oferenteId");

        if (id == null) {
            return "redirect:/login";
        }

        Oferente oferente = oferenteService.buscarPorId(id);

        if (oferente == null) {
            return "redirect:/login";
        }

        String estadoNombre = oferente.getEstado() != null ? oferente.getEstado().getNombre() : "";
        if (!"APROBADO".equalsIgnoreCase(estadoNombre)) {
            return "redirect:/login?error=true";
        }

        return "oferente/DashboardOferente";
    }

    @PostMapping("/userform")
    public String submit(
            @RequestParam("identificacion") String identificacion,
            @RequestParam("nombre") String nombre,
            @RequestParam("primerAp") String primerAp,
            @RequestParam("nacionalidad") String nacionalidad,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("lugarResidencia") String lugarResidencia,
            @RequestParam("customerPassword") String clave,
            @RequestParam("customerPasswordConfirm") String claveConfirm,
            RedirectAttributes redirectAttributes) {
        if (!clave.equals(claveConfirm)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/oferente/userform";
        }
        String error = oferenteService.registrar(identificacion, nombre, primerAp, nacionalidad,
                telefono, correo, lugarResidencia, clave);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/oferente/userform";
        }
        return "redirect:/inicio?guardadoOferente=ok";
    }
    @GetMapping("/habilidades")
    public String habilidades(HttpSession session,
                              Model model,
                              @RequestParam(required = false) Integer actual) {

        String idUsuario = (String) session.getAttribute("oferenteId");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        // 1. HABILIDADES DEL USUARIO
        List<Oferentehabilidad> lista = oferenteHabilidadRepository.findByOferente(idUsuario);
        model.addAttribute("habilidades", lista);

        // 2. SUBHABILIDADES SEGÚN RUTA
        List<Caracteristica> subHabilidades;

        Caracteristica actualCat = null;

        if (actual == null) {
            subHabilidades = caracteristicaService.listarRaices();
        } else {
            actualCat = caracteristicaRepository.findById(actual).orElse(null);
            subHabilidades = caracteristicaService.listarHijos(actual);
        }

        model.addAttribute("subHabilidades", subHabilidades);

        // 3. SOLO HOJAS
        model.addAttribute("habilidadesFiltradas", caracteristicaService.listarHojas(subHabilidades));

        // 4. RUTA (breadcrumbs)
        List<Caracteristica> ruta = new ArrayList<>();

        Caracteristica temp = actualCat;
        while (temp != null) {
            ruta.add(0, temp);
            temp = temp.getIdPadre();
        }

        model.addAttribute("rutaActual", ruta);
        model.addAttribute("categoriaActual", actualCat);

        return "oferente/habilidades";
    }
}
