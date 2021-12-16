package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.UploadFile;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    @Query("select u from UploadFile u join fetch u.notice n where u.notice.id = :noticeId")
    List<UploadFile> findAllUploadFile(@Param("noticeId") Long id);
}
