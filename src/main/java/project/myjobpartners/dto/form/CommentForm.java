package project.myjobpartners.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentForm {

    @NotEmpty
    private String comment;
}
