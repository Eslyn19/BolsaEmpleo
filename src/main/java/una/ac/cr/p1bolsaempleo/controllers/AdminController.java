package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;

    public AdminController(EmpresaService empresaService, OferenteService oferenteService) {
        this.empresaService = empresaService;
        this.oferenteService = oferenteService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null) {
            return "redirect:/login";
        }
        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("empresasPendientes", empresaService.listarPendientes().size());
        model.addAttribute("oferentesPendientes", 0);
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

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
