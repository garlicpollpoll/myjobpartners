package project.myjobpartners.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.myjobpartners.entity.Info;
import project.myjobpartners.entity.Notice;

import java.util.List;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {

    @Query("select i from Info i join fetch i.member m order by i.id desc")
    List<Info> findAllContent(Pageable pageable);

    @Query("select i from Info i join fetch i.member m where i.notice = :notice order by i.id desc")
    List<Info> findAllInfoTop(@Param("notice") int notice);

    @Query("select i from Info i where i.id = :noticeId")
    Info findByInfoId(@Param("noticeId") Long noticeId);

    @Query("select i from Info i where i.title like concat('%', :search, '%') order by i.id desc")
    List<Info> findInfoBySearch(@Param("search") String search);
}
