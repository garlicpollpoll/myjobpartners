package project.myjobpartners.controller.board;

import ch.qos.logback.core.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;
import project.myjobpartners.dto.form.CommentForm;
import project.myjobpartners.dto.form.WriteForm;
import project.myjobpartners.entity.Member;
import project.myjobpartners.entity.Notice;
import project.myjobpartners.entity.NoticeComment;
import project.myjobpartners.entity.UploadFile;
import project.myjobpartners.repository.MemberRepository;
import project.myjobpartners.repository.NoticeCommentRepository;
import project.myjobpartners.repository.NoticeRepository;
import project.myjobpartners.repository.UploadFileRepository;
import project.myjobpartners.s3.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final UploadFileRepository uploadFileRepository;
    private final S3Uploader s3Uploader;
    private final NoticeCommentRepository noticeCommentRepository;

    @GetMapping("/notice")
    public String notice(Model model, @RequestParam(value = "page", defaultValue = "0")Integer pageNow) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        List<Notice> noticeTop = noticeRepository.findAllNoticeTop(1);
        int noticeTopSize = noticeTop.size();
        PageRequest pageable = PageRequest.of(pageNow, 10 - noticeTopSize);
        List<Notice> findContents = noticeRepository.findAllContent(pageable);

        pageNow += 1;

        long pageStart, pageEnd;

        long size = noticeRepository.count();

        long totalPage = 0;

        if (size % 10 == 0) {
            totalPage = size / 10;
        }
        else {
            totalPage = size / 10 + 1;
        }

        pageStart = pageNow - 2;
        pageEnd = pageNow + 2;

        if (pageStart == 0 || pageStart == -1) {
            pageStart = 1;
            if (totalPage < 5) {
                pageEnd = totalPage;
            }
            else {
                pageEnd = 5;
            }
        } else if (pageEnd == totalPage + 1) { // ????????? ?????? ??? ?????????
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // ????????? ?????????
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 4;
            }
        }

        Map<Long, Long> map = new LinkedHashMap<>();

        for (long i = pageStart; i <= pageEnd; i++) {
            map.put(i, i);
        }

        model.addAttribute("pageCount", map);
        model.addAttribute("lastPage", totalPage);

        model.addAttribute("noticeTop", noticeTop);
        model.addAttribute("board", findContents);
        return "board/notice/notice_main";
    }

    @PostMapping("/notice_write")
    public String write(@Validated @ModelAttribute("write")WriteForm form, BindingResult bindingResult,
                        HttpServletRequest request, MultipartHttpServletRequest mtRequest) throws IOException {
        List<MultipartFile> uploadFile = mtRequest.getFiles("file");

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        Member findMember = memberRepository.findMemberByEmail(email);

        WriteForm write = new WriteForm();
        write.setTitle(form.getTitle());
        write.setContent(form.getContent());

        if (bindingResult.hasErrors()) {
            return "board/notice/notice_write";
        }

        String now = LocalDateTime.now().toString();
        String substring = now.substring(0, 10);

        Notice notice = new Notice(findMember, form.getTitle(), findMember.getName(), substring, 0, form.getContent(), 0);
        noticeRepository.save(notice);

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    UploadFile file = new UploadFile(originalFilename, s3UploadFile, notice);
                    uploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/notice";
    }

    @GetMapping("/notice_content/{contentId}")
    @Transactional
    public String noticeContent(@PathVariable("contentId")Long contentId, Model model) throws MalformedURLException {
        Notice findNotice = noticeRepository.findByNoticeId(contentId);
        List<NoticeComment> noticeComment = noticeCommentRepository.findNoticeComment(contentId);

        List<UploadFile> uploadFiles = findNotice.getUploadFiles();

        findNotice.addCount();

        CommentForm comment = new CommentForm();

        model.addAttribute("comment", comment);
        model.addAttribute("notice", findNotice);
        model.addAttribute("uploadFile", uploadFiles);
        model.addAttribute("noticeComment", noticeComment);

        return "board/notice/notice_content";
    }

    @GetMapping("/attach")
    public ResponseEntity<byte[]> attach(@RequestParam("upload") String upload) throws IOException {
        return s3Uploader.download(upload);
    }

    @GetMapping("/notice_delete/{contentId}")
    public String delete(@PathVariable("contentId") Long id) {
        Notice findNotice = noticeRepository.findByNoticeId(id);
        List<NoticeComment> noticeComment = noticeCommentRepository.findNoticeComment(id);
        List<UploadFile> allUploadFile = uploadFileRepository.findAllUploadFile(id);
        for (int i = 0; i < noticeComment.size(); i++) {
            NoticeComment findNoticeComment = noticeComment.get(i);
            noticeCommentRepository.delete(findNoticeComment);
        }
        for (int i = 0; i < allUploadFile.size(); i++) {
            UploadFile file = allUploadFile.get(i);
            uploadFileRepository.delete(file);
        }
        noticeRepository.delete(findNotice);

        return "redirect:/notice";
    }

    @GetMapping("/notice_rewrite/{contentId}")
    public String rewrite(@PathVariable("contentId") Long id, Model model, HttpServletRequest request) {
        Notice findNotice = noticeRepository.findByNoticeId(id);

        model.addAttribute("write", findNotice);

        HttpSession session = request.getSession();
        session.setAttribute("contentId", id);

        return "board/notice/notice_rewrite";
    }

    @PostMapping("/notice_rewrite")
    @Transactional
    public String rewrite(@Validated @ModelAttribute("write") WriteForm form, BindingResult bindingResult,
                          HttpServletRequest request, MultipartHttpServletRequest mtRequest) throws IOException {
        List<MultipartFile> uploadFile = mtRequest.getFiles("file");

        WriteForm write = new WriteForm();
        write.setTitle(form.getTitle());
        write.setContent(form.getContent());

        HttpSession session = request.getSession();
        Long contentId = (Long) session.getAttribute("contentId");

        if (bindingResult.hasErrors()) {
            return "board/notice/notice_rewrite";
        }

        Notice findNotice = noticeRepository.findByNoticeId(contentId);
        List<UploadFile> findUploadFile = uploadFileRepository.findAllUploadFile(contentId);

        findNotice.setTitle(form.getTitle());
        findNotice.setContent(form.getContent());

        for (int i = 0; i < findUploadFile.size(); i++) {
            UploadFile file = findUploadFile.get(i);
            uploadFileRepository.delete(file);
        }

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    UploadFile file = new UploadFile(originalFilename, s3UploadFile, findNotice);
                    uploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/notice";
    }

    @GetMapping("/notice_top/{contentId}")
    @Transactional
    public String noticeTop(@PathVariable("contentId") Long id) {
        Notice findNotice = noticeRepository.findByNoticeId(id);
        findNotice.makeNotice();

        return "redirect:/notice";
    }

    @GetMapping("/notice_under/{contentId}")
    @Transactional
    public String noticeUnder(@PathVariable("contentId") Long id) {
        Notice findNotice = noticeRepository.findByNoticeId(id);
        findNotice.cancelNotice();

        return "redirect:/notice";
    }
}
