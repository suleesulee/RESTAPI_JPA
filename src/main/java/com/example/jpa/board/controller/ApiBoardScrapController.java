package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiBoardScrapController {

    private final BoardService boardService;

    // 게시글의 스크랩을 하는 API
    @PutMapping("/api/board/{id}/scrap")
    public ResponseEntity<?> boardScrap(@PathVariable Long id,
                                        @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(
                JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }
        return ResponseResult.result(boardService.scrap(id, email));
    }


    //게시글의 스크랩을 취소하는 API
    @DeleteMapping("/api/scrap/{id}")
    public ResponseEntity<?> deleteBoardScrap(@PathVariable Long id,
                                 @RequestHeader("F-TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(
                JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(boardService.removeScrap(id, email));
    }




}
