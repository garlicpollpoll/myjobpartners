package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.Inquiry;
import project.myjobpartners.entity.InquiryComment;
import project.myjobpartners.entity.NoticeComment;

import java.util.List;

public interface InquiryCommentRepository extends JpaRepository<InquiryComment, Long> {

    @Query("select c from InquiryComment c join fetch c.member m join fetch c.inquiry n where c.inquiry.id = :contentId")
    List<InquiryComment> findInquiryComment(@Param("contentId") Long contentId);
}
