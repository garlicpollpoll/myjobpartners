package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.NoticeComment;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {

    @Query("select c from NoticeComment c join fetch c.member m join fetch c.notice n where c.notice.id = :contentId")
    List<NoticeComment> findNoticeComment(@Param("contentId") Long contentId);
}
