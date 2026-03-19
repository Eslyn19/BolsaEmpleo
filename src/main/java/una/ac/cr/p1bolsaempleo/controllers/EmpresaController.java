package una.ac.cr.p1bolsaempleo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;

@Controller
@RequiredArgsConstructor
public class EmpresaController {
    private final EmpresaService empresaService;

    @PostMapping("/empresa")
    public String registrar(
            @RequestParam("customerName") String nombreEmpresa,
            @RequestParam("Pais") String localizacion,
            @RequestParam("customerResidencia") String correo,
            @RequestParam("customerPhone") String telefono,
            @RequestParam("descripcionId") String descripcion,
            @RequestParam("customerPassword") String password,
            @RequestParam("customerPasswordConfirm") String passwordConfirm,
            Model model
    ) {
        try {
            empresaService.registrarEmpresa(
                    nombreEmpresa,
                    localizacion,
                    correo,
                    telefono,
                    descripcion,
                    password,
                    passwordConfirm
            );
            return "redirect:/inicio?guardadoEmpresa=ok";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "FormEmpresa";
        }
    }
}

