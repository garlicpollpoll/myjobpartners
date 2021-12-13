package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.myjobpartners.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {


}
