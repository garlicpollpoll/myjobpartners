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
    private String password;
    private String name;

    private String residentNum;

    public Member(String email, String password, String name, String residentNum) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.residentNum = residentNum;
    }
}
