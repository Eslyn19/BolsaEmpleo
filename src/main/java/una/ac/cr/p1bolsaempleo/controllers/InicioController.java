package una.ac.cr.p1bolsaempleo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.PuestoService;

import java.util.List;

@Controller
public class InicioController {
    private final CaracteristicaService caracteristicaService;
    private final PuestoService puestoService;

    public InicioController(PuestoService puestoService, CaracteristicaService caracteristicaService) {
        this.puestoService = puestoService;
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("puestosDestacados", puestoService.listarCincoPublicosParaInicio());
        return "Inicio";
    }
    @GetMapping("/buscar")
    public String buscarPuestos(
            @RequestParam(required = false) List<Integer> caracteristicas,
            Model model) {

        // Características arbol
        var arbol = caracteristicaService.arbolHojasActivas();
        model.addAttribute("arbolCaracteristicas", arbol);

        // Guardar lo seleccionado para que queden marcadas
        model.addAttribute("caracteristicasSeleccionadas", caracteristicas);

        // Obtener puestos públicos filtrados
        var puestos = puestoService.buscarPublicosPorCaracteristicas(caracteristicas);
        model.addAttribute("puestos", puestos);

        return "buscar-puestos";
    }
}
