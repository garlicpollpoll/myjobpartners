package project.myjobpartners.dto.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AgreementForm {

    @NotNull
    private String useAgreement;

    @NotNull
    private String personalAgreement;
}
