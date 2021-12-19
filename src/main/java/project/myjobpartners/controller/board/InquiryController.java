package project.myjobpartners.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import project.myjobpartners.dto.form.CommentForm;
import project.myjobpartners.dto.form.WriteForm;
import project.myjobpartners.entity.*;
import project.myjobpartners.repository.*;
import project.myjobpartners.s3.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final InquiryUploadFileRepository inquiryUploadFileRepository;
    private final S3Uploader s3Uploader;
    private final InquiryCommentRepository inquiryCommentRepository;

    @GetMapping("/inquiry")
    public String notice(Model model, @RequestParam(value = "page", defaultValue = "0")Integer pageNow) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest pageable = PageRequest.of(pageNow, 10);
        List<Inquiry> findContents = inquiryRepository.findAllContent(pageable);

        pageNow += 1;

        long pageStart, pageEnd;

        long size = inquiryRepository.count();

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
        } else if (pageEnd == totalPage + 1) { // 마지막 하나 전 페이지
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // 마지막 페이지
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

        model.addAttribute("board", findContents);
        return "board/inquiry/inquiry_main";
    }

    @PostMapping("/inquiry_write")
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
            return "board/inquiry/inquiry_write";
        }

        String now = LocalDateTime.now().toString();
        String substring = now.substring(0, 10);

        Inquiry inquiry = new Inquiry(findMember, form.getTitle(), substring, 0, form.getContent());
        inquiryRepository.save(inquiry);

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    InquiryUploadFile file = new InquiryUploadFile(originalFilename, s3UploadFile, inquiry);
                    inquiryUploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/inquiry";
    }

    @GetMapping("/inquiry_content/{contentId}")
    @Transactional
    public String noticeContent(@PathVariable("contentId")Long contentId, Model model) throws MalformedURLException {
        Inquiry findNotice = inquiryRepository.findByInquiryId(contentId);
        List<InquiryComment> noticeComment = inquiryCommentRepository.findInquiryComment(contentId);

        List<InquiryUploadFile> uploadFiles = findNotice.getUploadFiles();

        findNotice.addCount();

        CommentForm comment = new CommentForm();

        model.addAttribute("comment", comment);
        model.addAttribute("notice", findNotice);
        model.addAttribute("uploadFile", uploadFiles);
        model.addAttribute("noticeComment", noticeComment);

        return "board/inquiry/inquiry_content";
    }

    @GetMapping("/inquiry_delete/{contentId}")
    public String delete(@PathVariable("contentId") Long id) {
        Inquiry findNotice = inquiryRepository.findByInquiryId(id);
        List<InquiryComment> noticeComment = inquiryCommentRepository.findInquiryComment(id);
        List<InquiryUploadFile> allUploadFile = inquiryUploadFileRepository.findAllInquiryUploadFile(id);
        for (int i = 0; i < noticeComment.size(); i++) {
            InquiryComment findInquiryComment = noticeComment.get(i);
            inquiryCommentRepository.delete(findInquiryComment);
        }
        for (int i = 0; i < allUploadFile.size(); i++) {
            InquiryUploadFile file = allUploadFile.get(i);
            inquiryUploadFileRepository.delete(file);
        }
        inquiryRepository.delete(findNotice);

        return "redirect:/inquiry";
    }

    @GetMapping("/inquiry_rewrite/{contentId}")
    public String rewrite(@PathVariable("contentId") Long id, Model model, HttpServletRequest request) {
        Inquiry findNotice = inquiryRepository.findByInquiryId(id);

        model.addAttribute("write", findNotice);

        HttpSession session = request.getSession();
        session.setAttribute("contentId", id);

        return "board/inquiry/inquiry_rewrite";
    }

    @PostMapping("/inquiry_rewrite")
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
            return "board/inquiry/inquiry_rewrite";
        }

        Inquiry findNotice = inquiryRepository.findByInquiryId(contentId);
        List<InquiryUploadFile> findUploadFile = inquiryUploadFileRepository.findAllInquiryUploadFile(contentId);

        findNotice.setTitle(form.getTitle());
        findNotice.setContent(form.getContent());

        for (int i = 0; i < findUploadFile.size(); i++) {
            InquiryUploadFile file = findUploadFile.get(i);
            inquiryUploadFileRepository.delete(file);
        }

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    InquiryUploadFile file = new InquiryUploadFile(originalFilename, s3UploadFile, findNotice);
                    inquiryUploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/inquiry";
    }
}
