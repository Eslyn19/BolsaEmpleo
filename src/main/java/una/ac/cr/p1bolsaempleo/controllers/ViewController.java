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
    @Autowired
    private ServiceLogin serviceLogin;

    @GetMapping("/inicio")
    public String getMainPage() {
        return "Inicio";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "Registro";
    }

    @GetMapping("/userform")
    public String getOferenteFormPage() {
        return "FormOferente";
    }

    @GetMapping("/empresa")
    public String getEmpresaPage() {
        return "FormEmpresa";
    }

    // Ejemplo de POST para registro (comentado por ahora)
    // @PostMapping("/userform")
    // public String procesarRegistro(Oferente oferente, Model model) {
    //     try {
    //         serviceOferente.oferentesAdd(oferente);
    //         return "redirect:/login";
    //     } catch (Exception e) {
    //         model.addAttribute("error", "Error al registrarse: " + e.getMessage());
    //         return "FormOferente";
    //     }
    // }
}

