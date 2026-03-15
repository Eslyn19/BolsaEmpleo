package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import una.ac.cr.p1bolsaempleo.services.ServiceLogin;
import una.ac.cr.p1bolsaempleo.services.ServiceOferente;

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

    // MISHELLE metodos
//    @PostMapping("/userform")
//    public String procesarRegistro(Oferente oferente, Model model) {
//    try {
//        serviceOferente.oferentesAdd(oferente); // guarda el nuevo usuario
//        return "redirect:/login"; // obliga al usuario a iniciar sesión
//    } catch (Exception e) {
//        model.addAttribute("error", "Error al registrarse: " + e.getMessage());
//        return "FormOferente"; // vuelve al formulario si falla
//    }
}
