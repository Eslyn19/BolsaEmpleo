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

}
