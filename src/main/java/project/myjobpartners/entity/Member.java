package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String loginPw;
    private String name;

    private String residentNum;

    public Member(String email, String loginPw, String name, String residentNum) {
        this.email = email;
        this.loginPw = loginPw;
        this.name = name;
        this.residentNum = residentNum;
    }
}
