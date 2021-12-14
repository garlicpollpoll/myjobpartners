package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    private String originalFileName;
    private String uploadFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public UploadFile(String originalFileName, String uploadFilename, Notice notice) {
        this.originalFileName = originalFileName;
        this.uploadFilename = uploadFilename;
        this.notice = notice;
    }
}
