package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private String writer;
    private LocalDateTime writeDate;
    private int count;

    public Notice(String title, String writer, LocalDateTime writeDate, int count) {
        this.title = title;
        this.writer = writer;
        this.writeDate = writeDate;
        this.count = count;
    }
}
