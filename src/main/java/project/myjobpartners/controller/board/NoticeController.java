package project.myjobpartners.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.myjobpartners.dto.form.WriteForm;
import project.myjobpartners.entity.Member;
import project.myjobpartners.entity.Notice;
import project.myjobpartners.repository.MemberRepository;
import project.myjobpartners.repository.NoticeRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/write")
    public String write(@Validated @ModelAttribute("write")WriteForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        Member findMember = memberRepository.findMemberByEmail(email);

        WriteForm write = new WriteForm();
        write.setTitle(form.getTitle());
        write.setContent(form.getContent());

        if (bindingResult.hasErrors()) {
            return "board/write";
        }

        Notice notice = new Notice(form.getTitle(), findMember.getName(), LocalDateTime.now(), 0, form.getContent());

        noticeRepository.save(notice);

        return "redirect:/notice";
    }
}
