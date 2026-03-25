package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import una.ac.cr.p1bolsaempleo.services.PuestoService;

@Controller
public class InicioController {

    private final PuestoService puestoService;

    public InicioController(PuestoService puestoService) {
        this.puestoService = puestoService;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("puestosDestacados", puestoService.listarCincoPublicosParaInicio());
        return "Inicio";
    }
}
