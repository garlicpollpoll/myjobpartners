package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long id;

    private String title;
    private String writeDate;
    private int count;

    @Lob
    private String content;

    @OneToMany(mappedBy = "info")
    private List<InfoUploadFile> uploadFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int notice;

    public Info(Member member, String title, String writeDate, int count, String content, int notice) {
        this.title = title;
        this.writeDate = writeDate;
        this.count = count;
        this.content = content;
        this.member = member;
        this.notice = notice;
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
