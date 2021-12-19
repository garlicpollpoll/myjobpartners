package project.myjobpartners.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.myjobpartners.dto.form.CommentForm;
import project.myjobpartners.entity.*;
import project.myjobpartners.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InquiryCommentController {

    private final InquiryCommentRepository inquiryCommentRepository;
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/inquiry_comment/{noticeContentId}")
    public String comment(@Validated @ModelAttribute("comment") CommentForm form, BindingResult bindingResult,
                          @PathVariable("noticeContentId") Long id, RedirectAttributes redirectAttributes,
                          HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        Member findMember = memberRepository.findMemberByEmail(email);

        CommentForm comment = new CommentForm();
        comment.setComment(form.getComment());

        redirectAttributes.addAttribute("contentId", id);

        if (bindingResult.hasErrors()) {
            return "redirect:/notice_content/{contentId}";
        }

        if (findMember == null) {
            bindingResult.reject("PleaseLogin");
            return "redirect:/notice_content/{contentId}";
        }

        Inquiry findNotice = inquiryRepository.findByInquiryId(id);

        String date = LocalDateTime.now().toString();
        String writeDate = date.substring(0, 10);

        InquiryComment noticeComment = new InquiryComment(form.getComment(), findNotice, findMember, writeDate);

        inquiryCommentRepository.save(noticeComment);

        return "redirect:/inquiry_content/{contentId}";
    }
}
