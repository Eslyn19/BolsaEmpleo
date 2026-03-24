package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;
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
    public String dashboard(Authentication auth, Model model) {

        String idUsuario = auth.getName(); // ← ESTE ES EL ID DEL ADMIN

        Administrador admin = administradorService.buscarPorId(idUsuario);

        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("empresasPendientes", empresaService.listarPendientes().size());
        model.addAttribute("oferentesPendientes", oferenteService.listarPendientes().size());

        model.addAttribute("adminNombre", admin.getNombre());

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
