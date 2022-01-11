package com.example.jpa.extra.controller;

import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.extra.model.KakaoTranslateInput;
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
public class ApiExtraKakaoController {

    @GetMapping("/api/extra/kakao/translate")
    public ResponseEntity<?> translate(@RequestBody KakaoTranslateInput kakaoTranslateInput) {

        String restApiKey = "";
        String url = "http://dapi/kakao.";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("src_lang", "kr");
        parameters.add("target_lang", "en");
        parameters.add("query", kakaoTranslateInput.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "kakaoAK" + restApiKey);

        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);

        return ResponseResult.success(responseEntity.getBody());

    }
}
