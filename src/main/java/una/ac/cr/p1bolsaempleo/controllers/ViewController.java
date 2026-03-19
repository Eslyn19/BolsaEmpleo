package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.services.ServiceLogin;
import una.ac.cr.p1bolsaempleo.services.ServiceOferente;
import una.ac.cr.p1bolsaempleo.models.Rol;

@Controller
public class ViewController {
    @Autowired
    private ServiceOferente serviceOferente;
    private ServiceLogin serviceLogin;

    // Redireccionamiento a Inicio.HTML
    @GetMapping("/inicio")
    public String getMainPage() {
        return "Inicio";
    }

    // Redireccionamiento a Registro.HTML
    @GetMapping("/login")
    public String getLoginPage() { return "Registro"; }

    // Redireccionamiento a FormOferente.HTML
    @GetMapping("/userform")
    public String getOferenteFormPage() {
        // "Postularme" desde Inicio siempre debe mostrar el formulario.
        return "FormOferente";
    }

    // Redireccionamiento a Dashboard.HTML
    @GetMapping("/Dashboard")
    public String getDashboardPage() {
        return "Dashboard";
    }

    // Redireccionamiento a FormEmpresa.HTML
    @GetMapping("/empresa")
    public String getEmpresaPage() { return "FormEmpresa"; }

    @PostMapping("/userform")
    public String registrarOferente(
            @RequestParam("nombre") String nombre,
            @RequestParam("primerAp") String primerAp,
            @RequestParam("nacionalidad") String nacionalidad,
            @RequestParam("identificacion") String identificacion,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("lugarResidencia") String lugarResidencia,
            @RequestParam("customerPassword") String password,
            @RequestParam("customerPasswordConfirm") String passwordConfirm,
            Model model
    ) {
        try {
            serviceOferente.registrarOferente(
                    nombre,
                    primerAp,
                    nacionalidad,
                    identificacion,
                    telefono,
                    correo,
                    lugarResidencia,
                    password,
                    passwordConfirm
            );
            return "redirect:/inicio?guardadoOferente=ok";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "FormOferente";
        }
    }
}
