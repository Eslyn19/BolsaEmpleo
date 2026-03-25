package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import java.util.List;

@Controller
@RequestMapping("/admin")
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

    @ModelAttribute
    public void adminEnNavbar(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated() && auth.getName() != null && !auth.getName().isBlank()) {
            Administrador admin = administradorService.buscarPorId(auth.getName());
            model.addAttribute("adminNombre", admin.getNombre());
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("empresasPendientes", empresaService.listarPendientes().size());
        model.addAttribute("oferentesPendientes", oferenteService.listarPendientes().size());

        return "admin/DashboardAdminView";
    }

    @GetMapping("/empresas")
    public String empresas(Authentication auth, Model model) {

        model.addAttribute("empresas", empresaService.listarPendientes());

        return "admin/EmpresasAdminView";
    }

    @GetMapping("/oferentes")
    public String oferentes(Authentication auth, Model model) {

        model.addAttribute("pendientes", oferenteService.listarPendientes());

        return "admin/OferentesAdminView";
    }

    @GetMapping("/caracteristicas")
    public String caracteristicas(@RequestParam(value = "parentId", required = false) Integer parentId, Model model) {
        Integer raizPasivaId = caracteristicaService.idRaizPasiva().orElse(null);
        List<Caracteristica> items = caracteristicaService.listarItemsVista(parentId);
        List<Caracteristica> padresSelect = caracteristicaService.opcionesPadre(parentId);

        String etiquetaCategoria = "raíces";
        Integer volverPid = null;
        boolean volverRaices = true;

        if (parentId != null) {
            Caracteristica actual = caracteristicaService.obtenerConPadre(parentId).orElse(null);
            if (actual != null) {
                etiquetaCategoria = actual.getNombre();
                if (actual.getIdPadre() != null) {
                    volverPid = actual.getIdPadre().getId();
                }
                volverRaices = caracteristicaService.volverEsRaices(actual, raizPasivaId);
            }
        }

        model.addAttribute("titulo", "Características");
        model.addAttribute("items", items);
        model.addAttribute("parentId", parentId);
        model.addAttribute("padresSelect", padresSelect);
        model.addAttribute("raizPasivaId", raizPasivaId);
        model.addAttribute("etiquetaCategoria", etiquetaCategoria);
        model.addAttribute("volverPid", volverPid);
        model.addAttribute("volverRaices", volverRaices);

        return "admin/CaracteristicasAdminView";
    }

    @PostMapping("/caracteristicas")
    public String crearCaracteristica(@RequestParam("nombre") String nombre,
                                      @RequestParam("idPadre") Integer idPadre) {
        caracteristicaService.crear(nombre, idPadre);
        return "redirect:/admin/caracteristicas?parentId=" + idPadre;
    }

    @PostMapping("/caracteristicas/{id}/toggle-activo")
    public String toggleActivoCaracteristica(@PathVariable Integer id,
                                             @RequestParam(value = "parentId", required = false) Integer parentId) {
        caracteristicaService.alternarActivo(id);
        if (parentId != null) {
            return "redirect:/admin/caracteristicas?parentId=" + parentId;
        }
        return "redirect:/admin/caracteristicas";
    }

    @PostMapping("/empresas/{id}/aprobar")
    public String aprobarEmpresa(@PathVariable String id, @RequestParam("tipo") String tipo) {
        empresaService.aprobar(id, tipo);
        return "redirect:/admin/empresas";
    }

    @PostMapping("/empresas/{id}/rechazar")
    public String rechazarEmpresa(@PathVariable String id) {
        empresaService.rechazar(id);
        return "redirect:/admin/empresas";
    }

    @PostMapping("/oferentes/{id}/aprobar")
    public String aprobarOferente(@PathVariable String id) {
        oferenteService.aprobar(id);
        return "redirect:/admin/oferentes";
    }

    @PostMapping("/oferentes/{id}/rechazar")
    public String rechazarOferente(@PathVariable String id) {
        oferenteService.rechazar(id);
        return "redirect:/admin/oferentes";
    }
}
