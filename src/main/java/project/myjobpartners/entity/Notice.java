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
    private String writer;
    private String writeDate;
    private int count;

    private String content;

    @OneToMany(mappedBy = "notice")
    private List<UploadFile> uploadFiles;

    private int notice;

    public Notice(String title, String writer, String writeDate, int count, String content, int notice) {
        this.title = title;
        this.writer = writer;
        this.writeDate = writeDate;
        this.count = count;
        this.content = content;
        this.notice = notice;
    }
}
