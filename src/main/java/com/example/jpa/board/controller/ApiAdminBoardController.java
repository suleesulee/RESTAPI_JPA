package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.model.ResponseMessage;
import com.example.jpa.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiAdminBoardController {

    private final BoardService boardService;

    //게시글 스크랩을 추가하는 API
    @GetMapping("/api/admin/board/badreport")
    public ResponseEntity<?> badReport(){
            List<BoardBadReport> list = boardService.badReportList();
            return ResponseResult.success(list);
    }

}
