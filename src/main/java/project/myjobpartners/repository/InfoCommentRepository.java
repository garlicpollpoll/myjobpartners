package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.InfoComment;
import project.myjobpartners.entity.NoticeComment;

import java.util.List;

public interface InfoCommentRepository extends JpaRepository<InfoComment, Long> {

    @Query("select c from InfoComment c join fetch c.member m join fetch c.info n where c.info.id = :contentId")
    List<InfoComment> findInfoComment(@Param("contentId") Long contentId);
}
