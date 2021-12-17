package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private String writeDate;
    private int count;

    @Lob
    private String content;

    @OneToMany(mappedBy = "notice")
    private List<UploadFile> uploadFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int notice;

    public Notice(Member member, String title, String writer, String writeDate, int count, String content, int notice) {
        this.title = title;
        this.writeDate = writeDate;
        this.count = count;
        this.content = content;
        this.notice = notice;
        this.member = member;
    }

    public void addCount() {
        this.count += 1;
    }

    public void makeNotice() {
        this.notice = 1;
    }

    public void cancelNotice() {
        this.notice = 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
