package com.example.jpa.notice.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.exception.AlreadyDeletedException;
import com.example.jpa.notice.exception.DuplicateNoticeException;
import com.example.jpa.notice.exception.NoticeNotFoundException;
import com.example.jpa.notice.model.NoticeDeleteInput;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    /*@PostMapping("/api/notice")
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
    }*/

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

    //////////////
    // 12월 17일 //
    //////////////

    /* 공지사항에 NoticeInput에서 받은 값을 등록*/
    /*@PostMapping("/api/notice")
    public void addNotice(@RequestBody NoticeInput noticeInput) {
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .hits(0)
                .likes(0)
                .regDate((LocalDateTime.now()))
                .build();

        noticeRepository.save(notice);
    }*/

    
    /* 공지사항에 등록하기전 Valid를 이용하여 Error을 감지 
    * Error들을 하나로 모아서 response 할수있도록 수정 
    * 처리과정에 주목할 필요가 있음
    * Valid에서 에러 발생시 Errors에 error가 떨어짐
    * ResponseError 클래스에 of 메소드 정의해서 더 깔끔하게 보냄
    * */

    /*@PostMapping("/api/notice")
    public ResponseEntity<Object> addNotice(@RequestBody @Valid NoticeInput noticeInput, Errors errors){
        /* 1. 필수 값이 없으면 에러처리(가장 간단한 처리)

        if (noticeInput.getTitle() == null || noticeInput.getTitle().length() < 1
        ||noticeInput.getContents() == null || noticeInput.getContents().length() < 1)
            return new ResponseEntity<>("입력값이 정확하지 않습니다.", HttpStatus.BAD_REQUEST);

        */


        /*if (errors.hasErrors()){
                //2. errors에서 Error를 받아서 그냥 보냄 약간 필요없는 것들도 들어가기에 정리가 필요함

                return new ResponseEntity<>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
        */
    
            //3. 필요한 내용만 모아서 에러를 날림
            /*List<ResponseError> responseErrors = new ArrayList<>();
                errors.getAllErrors().stream().forEach(e-> {
                ResponseError responseError = new ResponseError();
                responseError.setField(((FieldError)e).getField());
                responseError.setMessage(e.getDefaultMessage());
                responseErrors.add(responseError);
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
            */

            /* 4. of 메소드 생성 후 처리
            errors.getAllErrors().stream().forEach(e -> {
                responseErrors.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }


        //정상 저장
        noticeRepository.save(Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .hits(0)
                .likes(0)
                .regDate((LocalDateTime.now()))
                .build());

        return ResponseEntity.ok().build();
    }*/


    /* noticeInput이 Valid한 값이면 정상 저장시킴, Valid에 포함되지 못한다면 Error를 반환*/
    @PostMapping("/api/notice")
    public ResponseEntity<Object> addNotice(@RequestBody @Valid NoticeInput noticeInput, Errors errors) {
        if (errors.hasErrors()) {

            //에러를 모아서 날림
            List<ResponseError> responseErrors = new ArrayList<>();
            errors.getAllErrors().stream().forEach(e -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        //정상저장
        noticeRepository.save(Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .hits(0)
                .likes(0)
                .regDate((LocalDateTime.now()))
                .build());

        return ResponseEntity.ok().build();
    }
    
    /* Page를 이용 최신 공지사항을 {count}만큼 반환함 */
    @GetMapping("/api/notice/latest/{count}")
    public Page<Notice> noticeLatest(@PathVariable int size) {
        Page<Notice> noticeList
                = noticeRepository.findAll(PageRequest.of(0, size, Sort.Direction.DESC, "regDate"));
        return noticeList;
    }

    @ExceptionHandler(DuplicateNoticeException.class)
    public ResponseEntity<?> handlerDuplicateNoticeException(DuplicateNoticeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* 따닥 클릭으로 게시물이 두번 등록되는 것을 방지
    * 동일 내용, 동일 타이틀로 1분내로 두번 등록되는 부분이 있다면 등록불가
    *  */
    @PostMapping("/api/notice")
    public void addNotice(@RequestBody NoticeInput noticeInput) {

        //중복체크
        //현재시간에 -1분했는데 동일제목, 동일 내용이라면 중복이라 판단. -> 등록못하게 함
        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1);

        /* JPA를 사용해서 findByTitleAndContentsAndRegDateIsGreaterThan 생성 그러나 notice가 있는지 없는지 카운트하는 방식이 더 나음
        Optional<List<Notice>> noticeList = noticeRepository.findByTitleAndContentsAndRegDateIsGreaterThan(
                noticeInput.getTitle(), noticeInput.getContents(), checkDate);
        if(noticeList.isPresent()){
            if(noticeList.get().size() > 0){
                throw new DuplicateNoticeException("1분 이내에 등록된 동일한 공지사항이 존재 합니다.");
            }
        }*/

        int noticeCount = noticeRepository.countByTitleAndContentsAndRegDateIsGreaterThanEqual(
                noticeInput.getTitle(), noticeInput.getContents(), checkDate);

        if(noticeCount > 0) {
            throw new DuplicateNoticeException("1분 이내에 등록된 동일한 공지사항이 존재 합니다.");
        }

        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .hits(0)
                .likes(0)
                .regDate((LocalDateTime.now()))
                .build();

        noticeRepository.save(notice);
    }

}
