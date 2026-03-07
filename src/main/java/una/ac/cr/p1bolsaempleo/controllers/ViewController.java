package una.ac.cr.p1bolsaempleo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping({"/", "/MainView.html"})
    public String mainView() {
        return "MainView.html";
    }

    @RequestMapping("/")
    public String loginView() {
        return "LoginView";
    }
}
