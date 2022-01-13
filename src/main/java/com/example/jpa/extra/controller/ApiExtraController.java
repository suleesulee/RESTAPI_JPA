package com.example.jpa.extra.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.extra.model.AirInput;
import com.example.jpa.extra.model.OpenApiResult;
import com.example.jpa.extra.model.PharmacySearch;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.model.ResponseMessage;
import com.example.jpa.util.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URLEncoder;
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

    //결과 데이터를 Json모델로 매핑하여 처리
    @GetMapping("/api/extra/pharmacy2")
    public ResponseEntity<?> pharmacy2() {

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

        OpenApiResult jsonResult = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonResult = objectMapper.readValue(apiResult, OpenApiResult.class);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseResult.success(jsonResult);
    }
    
    //시도/구군 단위 검색기능에 대한 구현을 추가

    @GetMapping("/api/extra/pharmacy3")
    public ResponseEntity<?> pharmacy3(@RequestBody PharmacySearch pharmacySearch) {

        String apiKey = "";
        String url = String.format("http://");
        String apiResult = "";

        try {

            url += String.format("&Q0=%s&Q1=%s", URLEncoder.encode(pharmacySearch.getSearchSido(), "UTF-8"),
                    URLEncoder.encode(pharmacySearch.getSearchGugun(), "UTF-8"));

            URI uri = new URI(url);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String result = restTemplate.getForObject(uri, String.class);
            apiResult = result;

        } catch (Exception e){
            e.printStackTrace();;
        }

        OpenApiResult jsonResult = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonResult = objectMapper.readValue(apiResult, OpenApiResult.class);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseResult.success(jsonResult);
    }

    //미세먼지 조회
    @GetMapping("/api/extra/air")
    public ResponseEntity<?> air(@RequestBody AirInput airInput){
        String apiKey = "";
        String url = String.format("http://");

        String apiResult = "";

        try {

            URI uri = new URI(String.format(url, apiKey, URLEncoder.encode("서울", "UTF-8")));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String result = restTemplate.getForObject(uri, String.class);
            apiResult = result;

        } catch (Exception e){
            e.printStackTrace();
        }

        return ResponseResult.success(apiResult);

    }


}
