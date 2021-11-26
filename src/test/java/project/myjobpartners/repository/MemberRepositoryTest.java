package project.myjobpartners.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.myjobpartners.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void test() throws Exception {
        //given
        Member member = new Member("경석", 24);
        //when
        memberRepository.save(member);
        //then
        Assertions.assertThat(member.getName()).isEqualTo("경석");
    }
}