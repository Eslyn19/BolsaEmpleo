package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.*;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;
import una.ac.cr.p1bolsaempleo.services.ServiceOferente;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final ServiceOferente serviceOferente;
    private final EmpresaService empresaService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");

        model.addAttribute("titulo", "Dashboard Admin");
        model.addAttribute("adminEmail", adminNombre != null ? adminNombre : "Admin");
        model.addAttribute("empresasPendientes", empresaService.listarPendientes().size());
        model.addAttribute("oferentesPendientes", serviceOferente.listarPendientes().size());
        return "/admin/DashboardAdminView";
    }

    @PostMapping("/login")
    public String login(@RequestParam String identificacion,
                        @RequestParam String clave,
                        HttpSession session,
                        Model model){
        Optional<Administrador> admin = administradorService.login(identificacion, clave);

        if(admin.isPresent()) {
            session.setAttribute("adminNombre", admin.get().getNombre());
            session.setAttribute("adminId", admin.get().getIdUsuario());
            return "redirect:/admin/dashboard";
        }

        Optional<Oferente> autenticado = serviceOferente.autenticarOferente(identificacion, clave);
        if (autenticado.isPresent()) {
            Oferente o = autenticado.get();
            if (o.getEstado() == null) {
                model.addAttribute("error", "Tu solicitud no tiene estado configurado.");
                return "Registro";
            }

            if (EstadoAprobacion.APROBADO.name().equals(o.getEstado().getNombre())) {
                session.setAttribute("usuarioRol", Rol.OFERENTE);
                session.setAttribute("usuarioId", o.getIdUsuario());
                session.setAttribute("usuarioNombre", o.getNombre());
                session.setAttribute("usuarioPrimerAp", o.getApellido());
                return "redirect:/login/DashboardOferente";
            }

            if (EstadoAprobacion.PENDIENTE.name().equals(o.getEstado().getNombre())) {
                model.addAttribute("error", "Tu solicitud está pendiente de aprobación por el administrador.");
                return "Registro";
            }

            if (EstadoAprobacion.RECHAZADO.name().equals(o.getEstado().getNombre())) {
                model.addAttribute("error", "Tu solicitud fue rechazada. Contacta soporte si crees que es un error.");
                return "Registro";
            }
        }

        model.addAttribute("error","Datos incorrectos");
        return "Registro";
    }

    @GetMapping("/oferentes")
    public String oferentesPendientes(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");
        List<una.ac.cr.p1bolsaempleo.models.Oferente> pendientes = serviceOferente.listarPendientes();
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("adminEmail", adminNombre != null ? adminNombre : "Admin");
        return "/admin/OferentesAdminView";
    }

    @GetMapping("/empresas")
    public String empresasPendientes(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");
        List<Empresa> pendientes = empresaService.listarPendientes();
        model.addAttribute("empresas", pendientes);
        model.addAttribute("adminEmail", adminNombre != null ? adminNombre : "Admin");
        return "/admin/EmpresasAdminView";
    }

    @GetMapping("/caracteristicas")
    public String caracteristicas(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");
        model.addAttribute("adminEmail", adminNombre != null ? adminNombre : "Admin");
        return "/admin/CaracteristicasAdminView";
    }

    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");
        model.addAttribute("adminEmail", adminNombre != null ? adminNombre : "Admin");
        return "/admin/ReportesAdminView";
    }

    @PostMapping("/oferentes/{id}/aprobar")
    public String aprobar(@PathVariable String id, HttpSession session) {
        String adminId = (String) session.getAttribute("adminId");
        serviceOferente.cambiarEstado(id, EstadoAprobacion.APROBADO, adminId);
        return "redirect:/admin/oferentes";
    }

    @PostMapping("/oferentes/{id}/rechazar")
    public String rechazar(@PathVariable String id, HttpSession session) {
        String adminId = (String) session.getAttribute("adminId");
        serviceOferente.cambiarEstado(id, EstadoAprobacion.RECHAZADO, adminId);
        return "redirect:/admin/oferentes";
    }

    @PostMapping("/empresas/{id}/aprobar")
    public String aprobarEmpresa(@PathVariable String id, HttpSession session) {
        String adminId = (String) session.getAttribute("adminId");
        empresaService.cambiarEstado(id, EstadoAprobacion.APROBADO, adminId);
        return "redirect:/admin/empresas";
    }

    @PostMapping("/empresas/{id}/rechazar")
    public String rechazarEmpresa(@PathVariable String id, HttpSession session) {
        String adminId = (String) session.getAttribute("adminId");
        empresaService.cambiarEstado(id, EstadoAprobacion.RECHAZADO, adminId);
        return "redirect:/admin/empresas";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio";
    }
}
