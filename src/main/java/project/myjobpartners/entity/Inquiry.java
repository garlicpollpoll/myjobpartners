package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    private String title;
    private String writeDate;
    private int count;

    @Lob
    private String content;

    @OneToMany(mappedBy = "inquiry")
    private List<InquiryUploadFile> uploadFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Inquiry(Member member, String title, String writeDate, int count, String content) {
        this.title = title;
        this.writeDate = writeDate;
        this.count = count;
        this.content = content;
        this.member = member;
    }

    public void addCount() {
        this.count += 1;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
