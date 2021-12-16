package com.example.jpa.notice.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeInput;
import com.example.jpa.notice.exception.AlreadyDeletedException;
import com.example.jpa.notice.exception.NoticeNotFoundException;
import com.example.jpa.notice.model.NoticeDeleteInput;
import com.example.jpa.notice.model.NoticeModel;
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ApiNoticeController {

    private final NoticeRepository noticeRepository;

    /*@GetMapping("/api/notice")
    public String noticeString() {
        return "공지사항입니다.";
    }*/

    /*
    @GetMapping("/api/notice")
    public NoticeModel notice(){
        LocalDateTime regDate = LocalDateTime.of(2021, 12, 2, 0 ,0);

        NoticeModel notice = new NoticeModel();
        notice.setId(1);
        notice.setContent("공지사항입니다.");
        notice.setRegDate(regDate);

        return notice;
    }*/
    /*
    @GetMapping("/api/notice")
    public List<NoticeModel> notice() {
        List<NoticeModel> noticeList = new ArrayList<>();
        NoticeModel notice1 = new NoticeModel();
        notice1.setId(1);
        notice1.setTitle("공지사항입니다.");
        notice1.setContent("공지사항내용입니다.");
        notice1.setRegDate(LocalDateTime.of(2021,1,30,0,0));
        noticeList.add(notice1);

        NoticeModel notice2 = NoticeModel.builder().id(2).title("두번쨰 공지사항입니다").build();
        notice1.setContent("두번째 공지사항내용입니다.");
        notice1.setRegDate(LocalDateTime.of(2021,1,30,0,0));
        noticeList.add(notice2);

        return noticeList;
    } */

//    @GetMapping("/api/notice")
//    public List<NoticeModel> notice() {
//        List<NoticeModel> noticeList = new ArrayList<>();
//
//        return noticeList;
//    }

    @GetMapping("/api/notice/count")
    public int noticeCount() {
        return 10;
    }

    /*@PostMapping("/api/notice")
    public NoticeModel addNotice(@RequestParam  String title, @RequestParam String contents){
        NoticeModel notice = NoticeModel.builder()
                .id(1)
                .title(title)
                .content(contents)
                .regDate(LocalDateTime.now())
                .build();

        return notice;
    }*/

    /*@PostMapping("/api/notice")
    public  NoticeModel addNotice(NoticeModel noticeModel){
        noticeModel.setId(2);
        noticeModel.setRegDate(LocalDateTime.of(2021,12,22,0,0));
        return noticeModel;
    }*/

    /*@PostMapping("/api/notice")
    public NoticeModel addNotice(@RequestBody NoticeModel noticeModel) {
        noticeModel.setId(3);
        noticeModel.setRegDate(LocalDateTime.now());

        return noticeModel;
    }*/

//    @PostMapping("/api/notice")
//    public Notice addNotice(@RequestBody NoticeInput noticeInput){
//        Notice notice = Notice.builder()
//                .title(noticeInput.getTitle())
//                .contents(noticeInput.getContents())
//                .regDate(LocalDateTime.now())
//                .build();
//
//        noticeRepository.save(notice);
//        return notice;
//    }

    @PostMapping("/api/notice")
    public Notice addNotice(@RequestBody NoticeInput noticeInput){
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .regDate(LocalDateTime.now())
                .hits(0)
                .likes(0)
                .build();

        Notice resultNotice = noticeRepository.save(notice);

        return resultNotice;
    }

    @GetMapping("/api/notice/{id}")
    public Notice notice(@PathVariable Long id){
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()){
            return notice.get();
        }
        return null;
    }

    /*@PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput){
        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()){
            notice.get().setTitle(noticeInput.getTitle());
            notice.get().setContents(noticeInput.getContents());
            notice.get().setUpdateDate(LocalDateTime.now());
            noticeRepository.save(notice.get());
        }
    }*/

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> handlerNoticeNotFoundException(NoticeNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput){
//        Notice notice = noticeRepository.findById(id)
//                .orElseThrow(()->new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()){
            notice.get().setTitle(noticeInput.getTitle());
            notice.get().setContents(noticeInput.getContents());
            notice.get().setUpdateDate(LocalDateTime.now());
            noticeRepository.save(notice.get());
        } else {
            throw new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다.");
        }
    }

    @PatchMapping("/api/notice/{id}/hits")
    public void noticeHits(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()->new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));
        notice.setHits(notice.getHits() + 1);

        noticeRepository.save(notice);
    }

    /*@DeleteMapping("/api/notice/{id}")
    public void deleteNotice(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()->new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        noticeRepository.deleteById(id);
    }*/

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

    @DeleteMapping("/api/notice/{id}")
    public void deleteNotice(@PathVariable Long id){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()->new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        if(notice.isDeleted()){
            throw new AlreadyDeletedException("이미 삭제된 글입니다.");
        }
        notice.setDeleted(true);
        notice.setDeletedDate(LocalDateTime.now());

        noticeRepository.save(notice);
    }

    /*여러글 한번에 삭제*/
    @DeleteMapping("/api/notice")
    public void deleteNoticeList(@RequestBody NoticeDeleteInput noticeDeleteInput) {
        List<Notice> noticeList = noticeRepository.findByIdIn(noticeDeleteInput.getIdList())
                .orElseThrow(()->new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        noticeList.stream().forEach(e -> {
            e.setDeleted(true);
            e.setDeletedDate(LocalDateTime.now());
        });

        noticeRepository.saveAll(noticeList);
    }

    @DeleteMapping("/api/notice/all")
    public void deleteAll() {
        noticeRepository.deleteAll();
    }


}
