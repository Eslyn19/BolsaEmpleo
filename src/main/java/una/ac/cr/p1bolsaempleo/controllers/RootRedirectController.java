package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("RootRedirectController")
public class RootRedirectController {
    @GetMapping("/")
    public String redirect(Model model){
        return "redirect:/inicio";
    }
}

