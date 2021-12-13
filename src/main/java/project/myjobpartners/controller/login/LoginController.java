package project.myjobpartners.controller.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.myjobpartners.dto.form.LoginForm;
import project.myjobpartners.entity.Member;
import project.myjobpartners.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("login")LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        LoginForm login = new LoginForm();
        login.setEmail(form.getEmail());
        login.setPassword(form.getPassword());

        if (bindingResult.hasErrors()) {
            return "member/login";
        }


        Member findMember = memberRepository.findMemberByEmailAndPassword(form.getEmail(), form.getPassword());

        if (findMember == null) {
            bindingResult.reject("notFound");
            return "member/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("email", form.getEmail());

        return "redirect:/";
    }
}
