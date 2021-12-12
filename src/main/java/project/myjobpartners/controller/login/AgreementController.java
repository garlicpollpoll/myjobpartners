package project.myjobpartners.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.myjobpartners.dto.form.AgreementForm;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AgreementController {

    @PostMapping("/agreement")
    public String agreement(@Validated @ModelAttribute("agreement")AgreementForm form, BindingResult bindingResult,
                            HttpServletRequest request) {
        String useAgreement = request.getParameter("use_agreement");
        String personalInfoAgreement = request.getParameter("personal_info_agreement");

        if (useAgreement == null || personalInfoAgreement == null) {
            bindingResult.reject("NotCheckAgreement");
            return "member/agreement";
        }

        return "redirect:/join";
    }
}
