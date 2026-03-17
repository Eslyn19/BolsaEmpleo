package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.models.Destreza;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.OferenteDestreza;
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
    public String cambiarRuta(@RequestParam("segmentoId") Integer segmentoId, Model model) {
        // Buscar la categoría/segmento en BD
        Destreza categoriaActual = serviceDestreza.buscarPorId(segmentoId);

        // Obtener la ruta completa hasta esa categoría
        List<Destreza> rutaActual = serviceDestreza.obtenerRuta(segmentoId);

        // Obtener subcategorías/habilidades hijas
        List<Destreza> subHabilidades = serviceDestreza.obtenerSubDestrezas(segmentoId);

        // Oferente actual (ejemplo)
        Oferente oferente = new Oferente();
        oferente.setId("10101010");
        List<OferenteDestreza> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("rutaActual", rutaActual);
        model.addAttribute("categoriaActual", categoriaActual);
        model.addAttribute("subHabilidades", subHabilidades);
        model.addAttribute("destrezasOferente", destrezasOferente);

        return "Habilidades";
    }

    @GetMapping("/habilidades")
    public String mostrarHabilidades(Model model) {
        // Oferente autenticado (ejemplo con ID fijo)
        Oferente oferente = new Oferente();
        oferente.setId("10101010");

        // Todas las destrezas de la BD
        List<Destreza> todasDestrezas = serviceDestreza.DestrezasAll();

        // Destrezas que ya tiene el oferente
        List<OferenteDestreza> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("todasDestrezas", todasDestrezas);
        model.addAttribute("destrezasOferente", destrezasOferente);

        return "Habilidades";
    }

    @PostMapping("/entrar")
    public String entrarHabilidad(@RequestParam("habilidadId") Integer habilidadId,
                                  Model model) {
        Oferente oferente = new Oferente();
        oferente.setId("10101010");

        // Si no la tiene, se agrega
        if (!serviceOferenteDestreza.tieneDestreza(oferente, habilidadId)) {
            Destreza destreza = serviceDestreza.buscarPorId(habilidadId);
            OferenteDestreza nueva = new OferenteDestreza();
            nueva.setOferente(oferente);
            nueva.setDestreza(destreza);
            nueva.setNivel(1);
            serviceOferenteDestreza.agregarDestreza(nueva);
        }

        // Recargar datos
        List<Destreza> todasDestrezas = serviceDestreza.DestrezasAll();
        List<OferenteDestreza> destrezasOferente = serviceOferenteDestreza.obtenerDestrezasPorOferente(oferente);

        model.addAttribute("todasDestrezas", todasDestrezas);
        model.addAttribute("destrezasOferente", destrezasOferente);

        return "Habilidades";
    }
}
