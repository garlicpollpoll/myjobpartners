package project.myjobpartners.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.myjobpartners.entity.Inquiry;
import project.myjobpartners.entity.Notice;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("select n from Inquiry n join fetch n.member m order by n.id desc")
    List<Inquiry> findAllContent(Pageable pageable);

    @Query("select n from Inquiry n where n.id = :noticeId")
    Inquiry findByInquiryId(@Param("noticeId") Long noticeId);
}
