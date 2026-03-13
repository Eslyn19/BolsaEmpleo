package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.logic.Oferente;
import una.ac.cr.p1bolsaempleo.logic.ServiceLogin;
import una.ac.cr.p1bolsaempleo.logic.ServiceOferente;

@Controller
public class ViewController {
    @Autowired
    private ServiceOferente serviceOferente;
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

    @GetMapping("/Dashboard")
    public String getDashboardPage() {
        return "Dashboard";
    }

//    @GetMapping("/empresa")
//    public String getEmpresaPage() {
//        return "EmpresaView";
//    }
    @PostMapping("/userform")
    public String procesarRegistro(Oferente oferente, Model model) {
    try {
        serviceOferente.oferentesAdd(oferente); // guarda el nuevo usuario
        return "redirect:/login"; // obliga al usuario a iniciar sesión
    } catch (Exception e) {
        model.addAttribute("error", "Error al registrarse: " + e.getMessage());
        return "FormOferente"; // vuelve al formulario si falla
    }
}

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
                                @RequestParam String clave,
                                Model model) {
        try {
            serviceLogin.autenticar(usuario, clave);
            return "redirect:/Dashboard"; // redirige si todo está bien
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "Registro"; // vuelve a la vista con mensaje
        }
    }


}
