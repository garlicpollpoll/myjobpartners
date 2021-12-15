package project.myjobpartners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.myjobpartners.dto.enums.DType;
import project.myjobpartners.entity.Member;
import project.myjobpartners.entity.Notice;
import project.myjobpartners.repository.MemberRepository;
import project.myjobpartners.repository.NoticeRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;

    @PostConstruct
    public void init() {
        Member member = new Member("kyoungsuk3254@naver.com", "123", "경석", "981007", "ADMIN", "남자");
        memberRepository.save(member);
        memberRepository.save(new Member("hkl0612@naver.com", "123", "임형규", "650310", "GUEST", "남자"));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  1));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  1));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
        noticeRepository.save(new Notice(member, "좀 긴 제목이 필요하니까 이정도로 쓰겠습니다.", "임경석", "2021-12-14", 0, "content",  0));
    }
}
