package project.myjobpartners.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryUploadFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    private String originalFileName;
    private String uploadFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    public InquiryUploadFile(String originalFileName, String uploadFilename, Inquiry inquiry) {
        this.originalFileName = originalFileName;
        this.uploadFilename = uploadFilename;
        this.inquiry = inquiry;
    }
}
