package project.myjobpartners.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
