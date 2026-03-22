package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final AdministradorService administradorService;
    private final EmpresaService empresaService;

    public LoginController(AdministradorService administradorService, EmpresaService empresaService) {
        this.administradorService = administradorService;
        this.empresaService = empresaService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "Registro";
    }

    @PostMapping("/ingreso")
    public String ingreso(
            @RequestParam("identificacion") String identificacion,
            @RequestParam("clave") String clave,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        var adminOpt = administradorService.login(identificacion, clave);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            session.removeAttribute("empresaId");
            session.removeAttribute("empresaEmail");
            session.removeAttribute("empresaNombre");
            session.setAttribute("adminId", admin.getIdUsuario());
            session.setAttribute("adminEmail", admin.getNombre());
            return "redirect:/admin/dashboard";
        }
        var empOpt = empresaService.login(identificacion, clave);
        if (empOpt.isPresent()) {
            Empresa emp = empOpt.get();
            session.removeAttribute("adminId");
            session.removeAttribute("adminEmail");
            session.setAttribute("empresaId", emp.getIdUsuario());
            session.setAttribute("empresaEmail", emp.getCorreo());
            session.setAttribute("empresaNombre", emp.getNombre());
            return "redirect:/empresa/dashboard";
        }
        redirectAttributes.addFlashAttribute("error",
                "Identificación o contraseña incorrecta, o cuenta empresa no aprobada.");
        return "redirect:/login";
    }
}
