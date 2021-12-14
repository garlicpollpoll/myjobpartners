package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myjobpartners.entity.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {


}
