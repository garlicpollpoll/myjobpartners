package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.InquiryUploadFile;
import project.myjobpartners.entity.UploadFile;

import java.util.List;

public interface InquiryUploadFileRepository extends JpaRepository<InquiryUploadFile, Long> {

    @Query("select u from InquiryUploadFile u join fetch u.inquiry n where u.inquiry.id = :noticeId")
    List<InquiryUploadFile> findAllInquiryUploadFile(@Param("noticeId") Long id);
}
