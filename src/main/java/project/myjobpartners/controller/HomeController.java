package project.myjobpartners.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.myjobpartners.dto.form.LoginForm;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/business")
    public String business() {
        return "business/main";
    }

    @GetMapping("/outsourcing")
    public String outsourcing() {
        return "business/outsourcing";
    }

    @GetMapping("/agent")
    public String agent() {
        return "business/agent";
    }

    @GetMapping("/contracting")
    public String contracting() {
        return "business/contracting";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginForm login = new LoginForm();
        model.addAttribute("login", login);
        return "member/login";
    }

    @GetMapping("/register")
    public String join() {
        return "member/join";
    }
}
