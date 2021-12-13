package project.myjobpartners.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.myjobpartners.dto.form.AgreementForm;
import project.myjobpartners.dto.form.JoinForm;
import project.myjobpartners.dto.form.LoginForm;
import project.myjobpartners.dto.form.WriteForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @GetMapping("/notice")
    public String notice() {
        return "board/notice";
    }

    @GetMapping("/info")
    public String info() {
        return "board/info";
    }

    @GetMapping("/inquiry")
    public String inquiry() {
        return "board/inquiry";
    }

    @GetMapping("/write")
    public String write(Model model, HttpServletRequest request) {
        WriteForm write = new WriteForm();
        model.addAttribute("write", write);
        return "board/write";
    }

    @GetMapping("/join")
    public String join(Model model) {
        JoinForm join = new JoinForm();
        model.addAttribute("join", join);
        return "member/join";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginForm login = new LoginForm();
        model.addAttribute("login", login);
        return "member/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        AgreementForm agreement = new AgreementForm();
        model.addAttribute("agreement", agreement);
        return "member/agreement";
    }
}
