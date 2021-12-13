package project.myjobpartners.controller.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.myjobpartners.dto.enums.DType;
import project.myjobpartners.dto.form.JoinForm;
import project.myjobpartners.entity.Member;
import project.myjobpartners.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final MemberRepository memberRepository;

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("join")JoinForm form, BindingResult bindingResult) {
        JoinForm join = new JoinForm();
        join.setEmail(form.getEmail());
        join.setPassword(form.getPassword());
        join.setPasswordCheck(form.getPasswordCheck());
        join.setName(form.getName());
        join.setBirth(form.getBirth());
        join.setGender(form.getGender());

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        Member member = new Member(form.getEmail(), form.getPassword(), form.getName(), form.getBirth(), DType.GUEST, form.getGender());

        memberRepository.save(member);

        return "redirect:/login";
    }
}
