package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.exception.AlreadyDeletedException;
import com.example.jpa.notice.exception.DuplicateNoticeException;
import com.example.jpa.notice.exception.NoticeNotFoundException;
import com.example.jpa.notice.model.NoticeDeleteInput;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.model.ResponseMessage;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.util.JWTUtils;
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
public class ApiBoardController {

    private final BoardService boardService;

    //게시판 추가 API
    @PostMapping("/api/board/type")
    public ResponseEntity<?> addBoardType(@RequestBody @Valid BoardTypeInput boardTypeInput, Errors errors) {

        if(errors.hasErrors()){
            List<ResponseError> responseErrors = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrors),
                    HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.addBoard(boardTypeInput);

        if(!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().build();

    }


    //게시판 타입명 수정하는 API
    @PutMapping("/api/board/type/{id}")
    public ResponseEntity<?> updateBoardType(@PathVariable Long id,
        @RequestBody @Valid BoardTypeInput boardTypeInput,
            Errors errors){

        if(errors.hasErrors()){
            List<ResponseError> responseErrors = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrors),
                    HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.updateBoard(id, boardTypeInput);

        if(!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().build();

    }


    //게시판타입 삭제 API
    @DeleteMapping("/api/board/type/{id}")
    public ResponseEntity<?> deleteBoardType(@PathVariable Long id) {

        ServiceResult result = boardService.deleteBoard(id);

        if(!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());
    }


    //게시판 타입 목록 리턴 API
    @GetMapping("/api/board/type")
    public ResponseEntity<?> boardType(){
        List<BoardType> boardTypeList = boardService.getAllBoardType();

        return ResponseEntity.ok().body(ResponseMessage.success(boardTypeList));
    }


    //게시판의 사용여부를 설정하는 API
    @PatchMapping("/api/board/type/{id}/using")
    public ResponseEntity<?> usingBoardType(@PathVariable Long id, @RequestBody BoardTypeUsing boardTypeUsing) {
        ServiceResult result = boardService.setBoardTypeUsing(id, boardTypeUsing);

        if(!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());
    }


    //게시판별 작성된 게시글의 개수를 리턴하는 API
    @GetMapping("/api/board/type/count")
    public ResponseEntity<?> boardTypeCount() {

        List<BoardTypeCount> list = boardService.getBoardTypeCount();

        return ResponseEntity.ok().body(list);
    }


    //게시된 게시글을 최상단에 배치하는 API
    @PatchMapping("/api/board/{id}/top")
    public ResponseEntity<?> boardPostTop(@PathVariable Long id) {
        ServiceResult result = boardService.setBoardTop(id, true);
        return ResponseEntity.ok().body(result);
    }


    //게시된 게시글을 최상단 해제하는 API
    @PatchMapping("/api/board/{id}/top")
    public ResponseEntity<?> boardPostClear(@PathVariable Long id) {
        ServiceResult result = boardService.setBoardTop(id, false);
        return ResponseEntity.ok().body(result);
    }


    //게시글의 게시기간을 시작일과 종요일로 설정할수있는 API
    @PatchMapping("/api/board/{id}/publish")
    public ResponseEntity<?> boardPeriod(@PathVariable Long id, @RequestBody BoardPeriod boardPeriod){
        ServiceResult result = boardService.setBoardPeriod(id, boardPeriod);

        if(!result.isResult()){
            return ResponseResult.fail(result.getMessage());
        }
        return ResponseResult.success();
    }

    
    //게시글 조회수를 증가시키는 API, JWT 인증을 통과한 사용자에 대해서 진행
    @PutMapping("/api/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable Long id, @RequestHeader("F-TOKEN") String token){

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardHits(id, email);
        if(result.isResult()){
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseResult.success();
    }


    //게시글의 좋아요  API
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id,  @RequestHeader("F-TOKEN") String token){
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardLike(id, email);
        return ResponseResult.result(result);
    }


    //게시글의 좋아요 취소 API
    @PutMapping("/api/board/{id}/unlike")
    public ResponseEntity<?> boardUnLike(@PathVariable Long id,  @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardUnLike(id, email);
        return ResponseResult.result(result);
    }

    //문제가 있는 게시글에 대해서 문제가 있는 게시글 신고기능 API
    @PutMapping("/api/board/{id}/badreport")
    public ResponseEntity<?> boardBadReport(@PathVariable Long id,
                               @RequestHeader("F-TOKEN") String token,
                               @RequestBody BoardBadReportInput boardBadReportInput){
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result =  boardService.addBadReport(id, email, boardBadReportInput);
        return ResponseResult.result(result);
    }


    //AOP Around를 사용하여 게시판 상세 조회에 대한 히스토리를 기록하는 기능
    @GetMapping("/api/board/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){

        Board board = null;
        try {
            board = boardService.detail(id);
        } catch (BizException e){
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success(board);

    }

    //인터셉터를 이용하여 API요청에 대한 정보를 log에 기록하는 기능
    @GetMapping("/api/board")
    public ResponseEntity<?> list(){

        List<Board> list = boardService.list();

        return ResponseResult.success(list);
    }

    //인터셉터를 이용하여 JWT인증이 필요한 API에 대해서(글쓰기) 토큰 유효성을 검증하는 API
    @PostMapping("/api/board")
    public ResponseEntity<?> list(@RequestHeader("F-TOKEN") String token,
                                  @RequestBody BoardInput boardInput){

        String email = JWTUtils.getIssuer(token);
        ServiceResult result = boardService.add(email, boardInput);
        return ResponseResult.result(result);
    }

    //게시판에 글을 작성했을 때 사용자에게 작서오딘 글의 정보를 메일로 전송하는 API
    @PostMapping("/api/board")
    public ResponseEntity<?> add(@RequestHeader("F-TOKEN") String token,
                                  @RequestBody BoardInput boardInput){

        String email = JWTUtils.getIssuer(token);
        ServiceResult result = boardService.add(email, boardInput);
        return ResponseResult.result(result);



    }}
