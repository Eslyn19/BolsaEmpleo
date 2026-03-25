package una.ac.cr.p1bolsaempleo.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import java.io.IOException;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EmpresaRepository empresaRepository;

    public LoginSuccessHandler(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession();
        String username = authentication.getName();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            session.setAttribute("adminId", username);
            response.sendRedirect("/admin/dashboard");

        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OFERENTE"))) {
            session.setAttribute("oferenteId", username);
            response.sendRedirect("/oferente/dashboard");

        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRESA"))) {
            session.setAttribute("empresaId", username);
            empresaRepository.findById(username).ifPresent(emp -> {
                session.setAttribute("empresaEmail", emp.getCorreo());
                session.setAttribute("empresaNombre", emp.getNombre());
            });
            response.sendRedirect("/empresa/dashboard");

        } else {
            response.sendRedirect("/dashboard");
        }
    }
}