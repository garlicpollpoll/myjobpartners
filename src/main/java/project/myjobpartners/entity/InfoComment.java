package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_comment_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id")
    private Info info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String writeDate;

    public InfoComment(String comment, Info info, Member member, String writeDate) {
        this.comment = comment;
        this.info = info;
        this.writeDate = writeDate;
        this.member = member;
    }
}
