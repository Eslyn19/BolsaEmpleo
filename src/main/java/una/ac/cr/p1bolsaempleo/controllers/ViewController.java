package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String getMainPage() {
        return "MainView";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "LoginView";
    }

}
