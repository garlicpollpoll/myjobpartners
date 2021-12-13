package project.myjobpartners.controller.counselling;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.myjobpartners.dto.form.CounselForm;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class CounselController {

    private final JavaMailSender javaMailSender;

    @PostMapping("/counselling")
    public String sendMail(@ModelAttribute CounselForm form) {
        if (form.getName().equals("") || form.getEmail().equals("") || form.getContent().equals("")) {
            return "redirect:/";
        }

        //수신할 대상을 담을 ArrayList 생성
        ArrayList<String> toUserList = new ArrayList<>();

        //수신 대상 추가
        toUserList.add("kyoungsuk3254@naver.com");

        //수신 대상 개수
        int toUserSize = toUserList.size();

        //SimpleMailMessage (단순 텍스트 구성 메일 메시지 생성할 때 이용)
        SimpleMailMessage simpleMessage = new SimpleMailMessage();

        //수신자 설정
        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        //메일 제목
        simpleMessage.setSubject("간단상담");

        //메일 내용
        simpleMessage.setText(form.getName() + " / " + form.getEmail() + " / " + form.getContent());

        //메일 발송
        javaMailSender.send(simpleMessage);

        return "redirect:/";
    }
}
