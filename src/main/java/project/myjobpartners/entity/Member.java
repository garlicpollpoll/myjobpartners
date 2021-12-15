package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.myjobpartners.dto.enums.DType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String name;

    private String birth;

    private String gender;

    private String dtype;

    public Member(String email, String password, String name, String birth, String dtype, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.dtype = dtype;
        this.gender = gender;
    }
}
