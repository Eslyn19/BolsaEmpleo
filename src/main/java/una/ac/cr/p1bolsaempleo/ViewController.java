package una.ac.cr.p1bolsaempleo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/MainView.html"})
    public String mainView() {
        // Reenvía internamente al recurso estático MainView.html
        return "forward:/MainView.html";
    }

    @GetMapping({"/login", "/LoginView.html"})
    public String loginView() {
        // Reenvía internamente al recurso estático LoginView.html
        return "forward:/LoginView.html";
    }
}
