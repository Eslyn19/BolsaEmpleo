package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.services.ServiceDestreza;
import una.ac.cr.p1bolsaempleo.services.ServiceOferenteDestreza;

import java.util.List;


@Controller
@RequestMapping("/oferente")
public class OferenteController {

    private final ServiceDestreza serviceDestreza;
    private final ServiceOferenteDestreza serviceOferenteDestreza;

    @Autowired
    public OferenteController(ServiceDestreza serviceDestreza,
                              ServiceOferenteDestreza serviceOferenteDestreza) {
        this.serviceDestreza = serviceDestreza;
        this.serviceOferenteDestreza = serviceOferenteDestreza;
    }
    @PostMapping("/dashboard")
    public String mostrarDashboard(@RequestParam String usuario,
                                   @RequestParam String clave,
                                   Model model) {
        // FALTA: validar usuario/clave con ServiceLogin si lo deseas

        return "DashboardOferente"; // busca DashboardOferente.html en templates
    }

    @PostMapping("/ruta")
    public String cambiarRuta(@RequestParam("segmentoId") Integer segmentoId,
                                HttpSession session,
                                Model model) {
        // Buscar la categoría/segmento en BD
        Caracteristica categoriaActual = serviceDestreza.buscarPorId(segmentoId);

        // Obtener la ruta completa hasta esa categoría
        List<Caracteristica> rutaActual = serviceDestreza.obtenerRuta(segmentoId);

        // Obtener subcategorías/habilidades hijas
        List<Caracteristica> subHabilidades = serviceDestreza.obtenerSubDestrezas(segmentoId);

        // Oferente actual (ejemplo)
        Oferente oferente = new Oferente();
        String oferenteId = (String) session.getAttribute("usuarioId");
        oferente.setIdUsuario(oferenteId != null ? oferenteId : "DEFAULT");
        List<Oferentehabilidad> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("rutaActual", rutaActual);
        model.addAttribute("categoriaActual", categoriaActual);
        model.addAttribute("subHabilidades", subHabilidades);
        model.addAttribute("destrezasOferente", destrezasOferente);
        model.addAttribute("habilidades", destrezasOferente);
        model.addAttribute("habilidadesFiltradas", serviceDestreza.DestrezasAll());

        return "Habilidades";
    }

    @GetMapping("/habilidades")
    public String mostrarHabilidades(HttpSession session, Model model) {
        // Oferente autenticado (ejemplo con ID fijo)
        Oferente oferente = new Oferente();
        String oferenteId = (String) session.getAttribute("usuarioId");
        oferente.setIdUsuario(oferenteId != null ? oferenteId : "DEFAULT");

        // Todas las destrezas de la BD
        List<Caracteristica> todasDestrezas = serviceDestreza.DestrezasAll();

        // Destrezas que ya tiene el oferente
        List<Oferentehabilidad> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("todasDestrezas", todasDestrezas);
        model.addAttribute("destrezasOferente", destrezasOferente);
        model.addAttribute("habilidades", destrezasOferente);
        model.addAttribute("habilidadesFiltradas", todasDestrezas);

        return "Habilidades";
    }

    @PostMapping("/entrar")
    public String entrarHabilidad(@RequestParam("habilidadId") Integer habilidadId,
                                    HttpSession session,
                                  Model model) {
        Oferente oferente = new Oferente();
        String oferenteId = (String) session.getAttribute("usuarioId");
        oferente.setIdUsuario(oferenteId != null ? oferenteId : "DEFAULT");

        // Si no la tiene, se agrega
        if (!serviceOferenteDestreza.tieneDestreza(oferente, habilidadId)) {
            Caracteristica caracteristica = serviceDestreza.buscarPorId(habilidadId);
            if (caracteristica != null) {
                Oferentehabilidad nueva = serviceOferenteDestreza.crearRelacion(oferente, caracteristica, 1);
                serviceOferenteDestreza.agregarDestreza(nueva);
            }
        }

        // Recargar datos
        List<Caracteristica> todasDestrezas = serviceDestreza.DestrezasAll();
        List<Oferentehabilidad> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("todasDestrezas", todasDestrezas);
        model.addAttribute("destrezasOferente", destrezasOferente);
        model.addAttribute("habilidades", destrezasOferente);
        model.addAttribute("habilidadesFiltradas", todasDestrezas);

        return "Habilidades";
    }
}
