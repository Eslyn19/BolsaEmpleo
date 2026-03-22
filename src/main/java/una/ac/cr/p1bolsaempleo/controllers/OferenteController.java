package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.services.OferenteService;

@Controller
public class OferenteController {

    private final OferenteService oferenteService;

    public OferenteController(OferenteService oferenteService) {
        this.oferenteService = oferenteService;
    }

    @GetMapping("/userform")
    public String form() {
        return "FormOferente";
    }

    @PostMapping("/userform")
    public String submit(
            @RequestParam("identificacion") String identificacion,
            @RequestParam("nombre") String nombre,
            @RequestParam("primerAp") String primerAp,
            @RequestParam("nacionalidad") String nacionalidad,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("lugarResidencia") String lugarResidencia,
            @RequestParam("customerPassword") String clave,
            @RequestParam("customerPasswordConfirm") String claveConfirm,
            RedirectAttributes redirectAttributes) {
        if (!clave.equals(claveConfirm)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/userform";
        }
        String error = oferenteService.registrar(identificacion, nombre, primerAp, nacionalidad,
                telefono, correo, lugarResidencia, clave);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/userform";
        }
        return "redirect:/inicio?guardadoOferente=ok";
    }
}
