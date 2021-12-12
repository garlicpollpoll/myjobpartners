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

    private String residentNum;

    @Enumerated(EnumType.STRING)
    private DType gender;

    public Member(String email, String password, String name, String residentNum, DType gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.residentNum = residentNum;
        this.gender = gender;
    }
}
