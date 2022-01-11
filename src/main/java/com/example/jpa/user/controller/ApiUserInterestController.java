package com.example.jpa.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.ExistsEmailException;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.UserService;
import com.example.jpa.util.JWTUtils;
import com.example.jpa.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApiUserInterestController {

    private final UserService userService;
    private final UserRepository userRepository;


    //78. 관심사용자를 등록하는 API
    @PutMapping("/api/user/{id}/interest")
    public ResponseEntity<?> interestUser(@PathVariable Long id,
            @RequestHeader("F-TOKEN") String token){

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(
                JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.addInterestUser(email, id);

        return ResponseResult.result(result);
    }

    //관심 사용자를 삭제하는 API
    @DeleteMapping("/api/user/interest/{id}")
    public ResponseEntity<?> deleteInterestUser(@PathVariable Long id,
                                          @RequestHeader("F-TOKEN") String token){

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch(
                JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.deleteInterestUser(email, id);

        return ResponseResult.result(result);
    }

}
