package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object adminNombre = session.getAttribute("adminNombre");
        if (adminNombre == null) {
            model.addAttribute("error", "Datos incorrectos");
            return "Registro";
        }

        model.addAttribute("titulo", "Dashboard Admin");
        model.addAttribute("adminEmail", adminNombre);
        return "admin/DashboardAdmin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String identificacion,
                        @RequestParam String clave,
                        HttpSession session,
                        Model model){
        Optional<Administrador> admin = administradorService.login(identificacion, clave);

        if(admin.isPresent()) {
            session.setAttribute("adminNombre", admin.get().getNombre());
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error","Datos incorrectos");
        return "Registro";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio";
    }
}
