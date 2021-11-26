package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myjobpartners.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
