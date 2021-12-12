package project.myjobpartners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.myjobpartners.dto.enums.DType;
import project.myjobpartners.entity.Member;
import project.myjobpartners.repository.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("kyoungsuk3254@naver.com", "ks32541007!", "경석", "981007-1", DType.ADMIN));
    }
}
