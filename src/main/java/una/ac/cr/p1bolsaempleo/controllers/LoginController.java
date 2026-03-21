package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    @GetMapping("/DashboardOferente")
    public String getDashboardOferente() {
        return "DashboardOferente";
    }
}
