package project.myjobpartners.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.myjobpartners.entity.Notice;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select n from Notice n join fetch n.member m order by n.id desc")
    List<Notice> findAllContent(Pageable pageable);

    @Query("select n from Notice n join fetch n.member m where n.notice = :notice order by n.id desc")
    List<Notice> findAllNoticeTop(@Param("notice") int notice);

    @Query("select n from Notice n where n.id = :noticeId")
    Notice findByNoticeId(@Param("noticeId") Long noticeId);
}
