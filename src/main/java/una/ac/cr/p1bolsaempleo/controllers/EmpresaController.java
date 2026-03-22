package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;

@Controller
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/empresa")
    public String form() {
        return "FormEmpresa";
    }

    @PostMapping("/empresa")
    public String submit(
            @RequestParam("correo") String correo,
            @RequestParam("customerName") String nombre,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam("customerPhone") String telefono,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("customerPassword") String clave,
            @RequestParam("customerPasswordConfirm") String claveConfirm,
            RedirectAttributes redirectAttributes) {
        if (!clave.equals(claveConfirm)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/empresa";
        }

        String error = empresaService.registrar(correo, nombre, ubicacion, telefono, descripcion, clave);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/empresa";
        }

        return "redirect:/inicio?guardadoEmpresa=ok";
    }
}
