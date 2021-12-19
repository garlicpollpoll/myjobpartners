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
public class InfoController {

    private final InfoRepository infoRepository;
    private final MemberRepository memberRepository;
    private final InfoUploadFileRepository infoUploadFileRepository;
    private final S3Uploader s3Uploader;
    private final InfoCommentRepository infoCommentRepository;

    @GetMapping("/info")
    public String notice(Model model, @RequestParam(value = "page", defaultValue = "0")Integer pageNow) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        List<Info> noticeTop = infoRepository.findAllInfoTop(1);
        int noticeTopSize = noticeTop.size();
        PageRequest pageable = PageRequest.of(pageNow, 10 - noticeTopSize);
        List<Info> findContents = infoRepository.findAllContent(pageable);

        pageNow += 1;

        long pageStart, pageEnd;

        long size = infoRepository.count();

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

        model.addAttribute("noticeTop", noticeTop);
        model.addAttribute("board", findContents);
        return "board/info/info_main";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("search") String search) {
        List<Info> noticeTop = infoRepository.findAllInfoTop(1);
        List<Info> findContents = infoRepository.findInfoBySearch(search);


        model.addAttribute("noticeTop", noticeTop);
        model.addAttribute("board", findContents);
        return "board/info/info_search_main";
    }

    @PostMapping("/info_write")
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
            return "board/info/info_write";
        }

        String now = LocalDateTime.now().toString();
        String substring = now.substring(0, 10);

        Info info = new Info(findMember, form.getTitle(), substring, 0, form.getContent(), 0);
        infoRepository.save(info);

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    InfoUploadFile file = new InfoUploadFile(originalFilename, s3UploadFile, info);
                    infoUploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/info";
    }

    @GetMapping("/info_content/{contentId}")
    @Transactional
    public String noticeContent(@PathVariable("contentId")Long contentId, Model model) throws MalformedURLException {
        Info findInfo = infoRepository.findByInfoId(contentId);
        List<InfoComment> noticeComment = infoCommentRepository.findInfoComment(contentId);

        List<InfoUploadFile> uploadFiles = findInfo.getUploadFiles();

        findInfo.addCount();

        CommentForm comment = new CommentForm();

        model.addAttribute("comment", comment);
        model.addAttribute("notice", findInfo);
        model.addAttribute("uploadFile", uploadFiles);
        model.addAttribute("noticeComment", noticeComment);

        return "board/info/info_content";
    }

    @GetMapping("/info_delete/{contentId}")
    public String delete(@PathVariable("contentId") Long id) {
        Info findNotice = infoRepository.findByInfoId(id);
        List<InfoComment> noticeComment = infoCommentRepository.findInfoComment(id);
        List<InfoUploadFile> allUploadFile = infoUploadFileRepository.findAllUploadFile(id);
        for (int i = 0; i < noticeComment.size(); i++) {
            InfoComment findNoticeComment = noticeComment.get(i);
            infoCommentRepository.delete(findNoticeComment);
        }
        for (int i = 0; i < allUploadFile.size(); i++) {
            InfoUploadFile file = allUploadFile.get(i);
            infoUploadFileRepository.delete(file);
        }
        infoRepository.delete(findNotice);

        return "redirect:/info";
    }

    @GetMapping("/info_rewrite/{contentId}")
    public String rewrite(@PathVariable("contentId") Long id, Model model, HttpServletRequest request) {
        Info findNotice = infoRepository.findByInfoId(id);

        model.addAttribute("write", findNotice);

        HttpSession session = request.getSession();
        session.setAttribute("contentId", id);

        return "board/info/info_rewrite";
    }

    @PostMapping("/info_rewrite")
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

        Info findNotice = infoRepository.findByInfoId(contentId);
        List<InfoUploadFile> findUploadFile = infoUploadFileRepository.findAllUploadFile(contentId);

        findNotice.setTitle(form.getTitle());
        findNotice.setContent(form.getContent());

        for (int i = 0; i < findUploadFile.size(); i++) {
            InfoUploadFile file = findUploadFile.get(i);
            infoUploadFileRepository.delete(file);
        }

        if (!uploadFile.isEmpty()) {
            for (MultipartFile multipartFile : uploadFile) {
                if (!multipartFile.getOriginalFilename().equals("")) {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String s3UploadFile = s3Uploader.upload(multipartFile, "static");
                    InfoUploadFile file = new InfoUploadFile(originalFilename, s3UploadFile, findNotice);
                    infoUploadFileRepository.save(file);
                    log.info("success");
                }
            }
        }

        return "redirect:/info";
    }

    @GetMapping("/info_top/{contentId}")
    @Transactional
    public String noticeTop(@PathVariable("contentId") Long id) {
        Info findNotice = infoRepository.findByInfoId(id);
        findNotice.makeNotice();

        return "redirect:/info";
    }

    @GetMapping("/info_under/{contentId}")
    @Transactional
    public String noticeUnder(@PathVariable("contentId") Long id) {
        Info findNotice = infoRepository.findByInfoId(id);
        findNotice.cancelNotice();

        return "redirect:/info";
    }
}
