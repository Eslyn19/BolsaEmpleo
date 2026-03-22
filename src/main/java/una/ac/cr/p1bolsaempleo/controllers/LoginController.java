package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final AdministradorService administradorService;

    public LoginController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "Registro";
    }

    @PostMapping("/admin/login")
    public String login(
            @RequestParam("identificacion") String identificacion,
            @RequestParam("clave") String clave,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        return administradorService.login(identificacion, clave)
                .map(admin -> {
                    session.setAttribute("adminId", admin.getIdUsuario());
                    session.setAttribute("adminEmail", admin.getNombre());
                    return "redirect:/admin/dashboard";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error",
                            "Identificación o contraseña incorrecta");
                    return "redirect:/login";
                });
    }
}
