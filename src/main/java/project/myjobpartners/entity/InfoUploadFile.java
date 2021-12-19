package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoUploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    private String originalFileName;
    private String uploadFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id")
    private Info info;

    public InfoUploadFile(String originalFileName, String uploadFilename, Info info) {
        this.originalFileName = originalFileName;
        this.uploadFilename = uploadFilename;
        this.info = info;
    }
}
