package project.myjobpartners.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordCheck;
    @NotEmpty
    private String birth;
    @NotEmpty
    private String gender;
    @NotEmpty
    private String name;
}
