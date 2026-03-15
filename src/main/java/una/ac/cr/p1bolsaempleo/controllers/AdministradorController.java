package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.Administrador;
import una.ac.cr.p1bolsaempleo.services.AdministradorService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdministradorController {

    private final AdministradorService administradorService;

    @PostMapping("/login")
    public String login(@RequestParam String identificacion, @RequestParam String clave, Model model){

        System.out.println("=== DEBUG ===");
        System.out.println("Identificacion: " + identificacion);

        Optional<Administrador> admin = administradorService.login(identificacion, clave);

        if(admin.isPresent()) { return "Dashboard"; }

        model.addAttribute("error","Credenciales incorrectas");
        return "Registro";
    }
}
