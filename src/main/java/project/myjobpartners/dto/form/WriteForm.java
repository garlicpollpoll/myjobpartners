package project.myjobpartners.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WriteForm {

    @NotEmpty
    private String title;

    private String content;
}
