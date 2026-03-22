package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;

    public AdminController(EmpresaService empresaService, OferenteService oferenteService,
                           CaracteristicaService caracteristicaService) {
        this.empresaService = empresaService;
        this.oferenteService = oferenteService;
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null) {
            return "redirect:/login";
        }
        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("empresasPendientes", empresaService.listarPendientes().size());
        model.addAttribute("oferentesPendientes", oferenteService.listarPendientes().size());
        model.addAttribute("adminEmail", adminEmail);
        return "admin/DashboardAdminView";
    }

    @GetMapping("/empresas")
    public String empresas(HttpSession session, Model model) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null) {
            return "redirect:/login";
        }
        model.addAttribute("empresas", empresaService.listarPendientes());
        model.addAttribute("adminEmail", adminEmail);
        return "admin/EmpresasAdminView";
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

    @GetMapping("/oferentes")
    public String oferentes(HttpSession session, Model model) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null) {
            return "redirect:/login";
        }
        model.addAttribute("pendientes", oferenteService.listarPendientes());
        model.addAttribute("adminEmail", adminEmail);
        return "admin/OferentesAdminView";
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

    @GetMapping("/caracteristicas")
    public String caracteristicas(HttpSession session, Model model,
                                  @RequestParam(required = false) Integer parentId) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null) {
            return "redirect:/login";
        }
        Optional<Integer> raizOpt = caracteristicaService.idRaizPasiva();
        model.addAttribute("items", caracteristicaService.listarItemsVista(parentId));
        model.addAttribute("padresSelect", caracteristicaService.opcionesPadre(parentId));
        model.addAttribute("parentId", parentId);
        model.addAttribute("raizPasivaId", raizOpt.orElse(null));
        model.addAttribute("adminEmail", adminEmail);
        String etiqueta = "raíces";
        if (parentId != null) {
            Optional<Caracteristica> cur = caracteristicaService.obtenerConPadre(parentId);
            if (cur.isPresent()) {
                etiqueta = cur.get().getNombre();
                boolean volverRaices = caracteristicaService.volverEsRaices(cur.get(), raizOpt.orElse(null));
                model.addAttribute("volverRaices", volverRaices);
                if (!volverRaices && cur.get().getIdPadre() != null) {
                    model.addAttribute("volverPid", cur.get().getIdPadre().getId());
                } else {
                    model.addAttribute("volverPid", null);
                }
            } else {
                model.addAttribute("volverRaices", true);
                model.addAttribute("volverPid", null);
            }
        } else {
            model.addAttribute("volverRaices", false);
            model.addAttribute("volverPid", null);
        }
        model.addAttribute("etiquetaCategoria", etiqueta);
        return "admin/CaracteristicasAdminView";
    }

    @PostMapping("/caracteristicas")
    public String crearCaracteristica(@RequestParam String nombre, @RequestParam Integer idPadre) {
        caracteristicaService.crear(nombre, idPadre);
        return "redirect:/admin/caracteristicas?parentId=" + idPadre;
    }

    @PostMapping("/caracteristicas/{id}/toggle-activo")
    public String toggleActivoCaracteristica(@PathVariable Integer id,
                                            @RequestParam(required = false) Integer parentId) {
        try {
            caracteristicaService.alternarActivo(id);
        } catch (Exception ignored) {
        }
        if (parentId != null) {
            return "redirect:/admin/caracteristicas?parentId=" + parentId;
        }
        return "redirect:/admin/caracteristicas";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
