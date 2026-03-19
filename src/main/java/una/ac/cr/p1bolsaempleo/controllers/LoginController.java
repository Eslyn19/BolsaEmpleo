package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.p1bolsaempleo.models.Login;
import una.ac.cr.p1bolsaempleo.services.ServiceLogin;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final ServiceLogin serviceLogin;

    @GetMapping("/DashboardOferente")
    public String getDashboardOferente() {
        return "DashboardOferente";
    }
}
