package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/inicio")
    public String getMainPage() {
        return "Inicio";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "LoginView";
    }

    @GetMapping("/userform")
    public String getOferenteFormPage() {
        return "FormOferente";
    }

//    @GetMapping("/empresa")
//    public String getEmpresaPage() {
//        return "EmpresaView";
//    }

}
