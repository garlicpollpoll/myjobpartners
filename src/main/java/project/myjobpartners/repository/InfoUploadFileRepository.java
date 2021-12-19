package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.myjobpartners.entity.InfoUploadFile;
import project.myjobpartners.entity.UploadFile;

import java.util.List;

public interface InfoUploadFileRepository extends JpaRepository<InfoUploadFile, Long> {

    @Query("select u from InfoUploadFile u join fetch u.info n where u.info.id = :noticeId")
    List<InfoUploadFile> findAllUploadFile(@Param("noticeId") Long id);
}
