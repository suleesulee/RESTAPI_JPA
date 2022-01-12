package com.example.jpa.extra.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.model.ResponseMessage;
import com.example.jpa.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiExtraController {

    private final BoardService boardService;

    @GetMapping("/api/extra/pharmacy")
    public String pharmacy() {

        String apiKey = "";
        String url = "http://";
        String apiResult = "";

        try {
            URI uri = new URI(String.format(url, apiKey));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


            String result = restTemplate.getForObject(uri, String.class);
            apiResult = result;

        } catch (Exception e){
            e.printStackTrace();;
        }

        return apiResult;
    }

}
