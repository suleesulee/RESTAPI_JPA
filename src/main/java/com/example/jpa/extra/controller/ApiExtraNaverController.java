package com.example.jpa.extra.controller;

import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.extra.model.KakaoTranslateInput;
import com.example.jpa.extra.model.NaverTranslateInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiExtraNaverController {

    @GetMapping("/api/extra/naver/translate")
    public ResponseEntity<?> translate(@RequestBody NaverTranslateInput naverTranslateInput) {

        String clientId = "";
        String clientSecret = "";

        String url = "http://openapi.naver.com/v1/papago/n2mt";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("source", "ko");
        parameters.add("target", "en");
        parameters.add("text", naverTranslateInput.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);


        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);

        return ResponseResult.success(responseEntity.getBody());

    }
}
