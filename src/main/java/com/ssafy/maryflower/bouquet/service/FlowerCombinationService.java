package com.ssafy.maryflower.bouquet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FlowerCombinationService {

    private final RestTemplate restTemplate;

    @Value("${chatgpt.api.endpoint}")
    private String apiEndpoint;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    public String callChatGptApi(String prompt){

        // HTTP 헤더 설정 : 수락 가능한 응답 타입, 컨텐츠 타입, 인증 정보를 설정.
        HttpHeaders headers=new HttpHeaders();

        // 응답으로 JSON을 받겠다는 의미.
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 요청 본문의 컨텐츠 타입이 JSON임을 나타냄.
        headers.setContentType(MediaType.APPLICATION_JSON);

        // API 인증을 위한 헤더, 여기서는 Bearer 토큰을 사용.
        headers.set("Authorization","Bearer "+apiKey);

        // 요청 바디 설정: 여기서 prompt를 JSON형식으로 포함.
        String requestBody="{\"prompt\": \"" + prompt + "\"}";

        // HttpEntity 객체를 사용하여 요청 본문과 헤더를 포함.
        HttpEntity<String> entity=new HttpEntity<>(requestBody,headers);

        // RestTemplate을 사용하여 HTTP POST 요청을 보냅니다. 이때 apiEndpoint, HttpMethod.POST, 요청 엔티티, 그리고 응답 타입을 지정합니다.
        ResponseEntity<String> response = restTemplate.exchange(
                apiEndpoint, // API 엔드포인트
                HttpMethod.POST, // HTTP 메서드
                entity,  // 요청 본문과 헤더를 포함하는 HttpEntity 객체
                String.class); // 응답 본문의 예상 타입.

        // API로부터 받은 응답 본문을 문자열로 반환합니다.
        return response.getBody();
    }

}
