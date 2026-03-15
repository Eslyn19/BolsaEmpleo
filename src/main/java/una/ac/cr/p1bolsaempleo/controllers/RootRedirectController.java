package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("RootRedirectController")
public class RootRedirectController {

    @GetMapping("/")
    public String inicio(){
        return "Inicio";
    }
}

