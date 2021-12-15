package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_comment_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String writeDate;

    public NoticeComment(String comment, Notice notice, Member member, String writeDate) {
        this.comment = comment;
        this.notice = notice;
        this.writeDate = writeDate;
        this.member = member;
    }
}
