package project.myjobpartners.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.myjobpartners.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByEmailAndPassword(String email, String password);
}
