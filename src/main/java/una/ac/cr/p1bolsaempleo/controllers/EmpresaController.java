package una.ac.cr.p1bolsaempleo.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import una.ac.cr.p1bolsaempleo.services.CaracteristicaService;
import una.ac.cr.p1bolsaempleo.services.EmpresaService;
import una.ac.cr.p1bolsaempleo.services.PuestoService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    private final EmpresaService empresaService;
    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;

    public EmpresaController(EmpresaService empresaService, PuestoService puestoService,
                             CaracteristicaService caracteristicaService) {
        this.empresaService = empresaService;
        this.puestoService = puestoService;
        this.caracteristicaService = caracteristicaService;
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

    @GetMapping("/empresa/login")
    public String loginForm() {
        return "redirect:/login";
    }

    @GetMapping("/empresa/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        model.addAttribute("empresaEmail", session.getAttribute("empresaEmail"));
        model.addAttribute("empresaNombre", session.getAttribute("empresaNombre"));
        return "empresa/DashboardEmpresa";
    }

    @GetMapping("/empresa/puestos")
    public String misPuestos(HttpSession session, Model model) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        String id = (String) session.getAttribute("empresaId");
        model.addAttribute("empresaEmail", session.getAttribute("empresaEmail"));
        model.addAttribute("empresaNombre", session.getAttribute("empresaNombre"));
        model.addAttribute("puestos", puestoService.listarPorEmpresa(id));
        return "empresa/MisPuestosEmpresa";
    }

    @GetMapping("/empresa/puestos/publicar")
    public String publicarPuestoForm(HttpSession session, Model model) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        model.addAttribute("empresaEmail", session.getAttribute("empresaEmail"));
        model.addAttribute("empresaNombre", session.getAttribute("empresaNombre"));
        model.addAttribute("caracteristicasDisponibles", caracteristicaService.listarActivasParaSeleccionPuesto());
        return "empresa/PublicarPuestoEmpresa";
    }

    @PostMapping("/empresa/puestos")
    public String crearPuesto(
            HttpSession session,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("salario") Double salario,
            @RequestParam(name = "idCaracteristica", required = false) String[] idCaracteristicaRaw,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        try {
            List<Integer> idsCaracteristicas = parseIdsCaracteristicas(idCaracteristicaRaw);
            puestoService.crear((String) session.getAttribute("empresaId"), descripcion, salario, idsCaracteristicas);
            redirectAttributes.addFlashAttribute("msgOk", "Puesto publicado correctamente.");
        } catch (Exception e) {
            log.error("No se pudo guardar el puesto", e);
            String detalle = mensajeCausaRaiz(e);
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo guardar el puesto. Detalle: " + detalle
                            + " — Comprueba en MySQL: tabla puesto_caracteristica (FK idPuesto, idCaracteristica) y columna Puesto.idUsuario VARCHAR(50) si usas correo como id.");
            return "redirect:/empresa/puestos/publicar";
        }
        return "redirect:/empresa/puestos";
    }

    /** Varios checkboxes con el mismo nombre llegan como String[] (más fiable que List<Integer>). */
    private static List<Integer> parseIdsCaracteristicas(String[] raw) {
        if (raw == null || raw.length == 0) {
            return null;
        }
        List<Integer> ids = new ArrayList<>();
        for (String s : raw) {
            if (s == null || s.isBlank()) {
                continue;
            }
            try {
                ids.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {
                /* ignorar valor inválido */
            }
        }
        return ids.isEmpty() ? null : ids;
    }

    private static String mensajeCausaRaiz(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        String m = t.getMessage();
        if (m == null || m.isBlank()) {
            m = e.getMessage();
        }
        if (m == null) {
            return e.getClass().getSimpleName();
        }
        if (m.length() > 450) {
            return m.substring(0, 450) + "…";
        }
        return m;
    }

    @PostMapping("/empresa/puestos/{id}/desactivar")
    public String desactivarPuesto(HttpSession session, @PathVariable Integer id) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        try {
            puestoService.desactivar((String) session.getAttribute("empresaId"), id);
        } catch (Exception ignored) {
        }
        return "redirect:/empresa/puestos";
    }

    @PostMapping("/empresa/puestos/{id}/activar")
    public String activarPuesto(HttpSession session, @PathVariable Integer id) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        try {
            puestoService.activar((String) session.getAttribute("empresaId"), id);
        } catch (Exception ignored) {
        }
        return "redirect:/empresa/puestos";
    }

    @PostMapping("/empresa/puestos/{id}/acceso-publico")
    public String accesoPublico(HttpSession session, @PathVariable Integer id) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        try {
            puestoService.marcarAccesoPublico((String) session.getAttribute("empresaId"), id);
        } catch (Exception ignored) {
        }
        return "redirect:/empresa/puestos";
    }

    @PostMapping("/empresa/puestos/{id}/acceso-privado")
    public String accesoPrivado(HttpSession session, @PathVariable Integer id) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        try {
            puestoService.marcarAccesoPrivado((String) session.getAttribute("empresaId"), id);
        } catch (Exception ignored) {
        }
        return "redirect:/empresa/puestos";
    }

    @GetMapping("/empresa/puestos/{id}/candidatos")
    public String candidatosPuesto(HttpSession session, Model model, RedirectAttributes redirectAttributes,
                                   @PathVariable Integer id) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        String idEmpresa = (String) session.getAttribute("empresaId");
        model.addAttribute("empresaEmail", session.getAttribute("empresaEmail"));
        model.addAttribute("empresaNombre", session.getAttribute("empresaNombre"));

        return puestoService.obtenerPuestoEmpresaParaCandidatos(id, idEmpresa)
                .filter(p -> p.getOferenteAsignado() == null
                        && p.getActivo() != null
                        && p.getActivo() == 1)
                .map(p -> {
                    model.addAttribute("puesto", p);
                    model.addAttribute("candidatos", puestoService.listarCandidatosCompatibles(p));
                    return "empresa/CandidatosPuestoEmpresa";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error",
                            "Este puesto no admite búsqueda de candidatos (cerrado, ya asignado o no existe).");
                    return "redirect:/empresa/buscar-puestos";
                });
    }

    @PostMapping("/empresa/puestos/{id}/asignar-candidato")
    public String asignarCandidato(HttpSession session,
                                   @PathVariable Integer id,
                                   @RequestParam("idOferente") String idOferente,
                                   RedirectAttributes redirectAttributes) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        String idEmpresa = (String) session.getAttribute("empresaId");
        try {
            puestoService.asignarOferenteYCerrarPuesto(idEmpresa, id, idOferente.trim());
            redirectAttributes.addFlashAttribute("msgOk", "Candidato aceptado. El puesto quedó asignado y cerrado.");
        } catch (Exception e) {
            log.warn("No se pudo asignar candidato al puesto {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo asignar el candidato: " + mensajeCausaRaiz(e));
            return "redirect:/empresa/puestos/" + id + "/candidatos";
        }
        return "redirect:/empresa/buscar-puestos";
    }

    @GetMapping("/empresa/buscar-puestos")
    public String buscarPuestos(HttpSession session, Model model) {
        if (session.getAttribute("empresaId") == null) {
            return "redirect:/login";
        }
        String id = (String) session.getAttribute("empresaId");
        model.addAttribute("empresaEmail", session.getAttribute("empresaEmail"));
        model.addAttribute("empresaNombre", session.getAttribute("empresaNombre"));
        model.addAttribute("puestos", puestoService.listarAbiertosParaBuscarCandidatos(id));
        return "empresa/BuscarPuestosEmpresa";
    }

    @PostMapping("/empresa/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio";
    }
}
