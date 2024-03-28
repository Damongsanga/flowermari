package com.ssafy.maryflower.global.auth.service;


import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthMemberInfoResponse;
import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthAccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class KaKaoOAuthClient implements OAuthClient {

    private final String accessTokenUrl;

    private final String memberInfoUrl;
    private static final RestTemplate restTemplate = new RestTemplate();
    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;

    public KaKaoOAuthClient(@Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String accessTokenUrl,
                            @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String memberInfoUrl,
                            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUrl,
                            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                            @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret) {
        this.accessTokenUrl = accessTokenUrl;
        this.memberInfoUrl = memberInfoUrl;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        this.clientSecret = clientSecret;
    }

    @Override
    public KakaoOAuthAccessTokenResponse getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoOAuthAccessTokenResponse> response =
                restTemplate.postForEntity(accessTokenUrl, request, KakaoOAuthAccessTokenResponse.class);

        String accessToken = Objects.requireNonNull(response.getBody()).getAccessToken();

        return new KakaoOAuthAccessTokenResponse(accessToken);

    }

    @Override
    public KakaoOAuthMemberInfoResponse getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        KakaoOAuthMemberInfoResponse res = restTemplate.exchange(
                memberInfoUrl,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                KakaoOAuthMemberInfoResponse.class
        ).getBody();

        log.info("id : {}", res.getOauthId());
        log.info("name");
        log.info("kakaoAccount : {}", res.getKakaoAccount());

        log.info("name : {}", res.getNickname());
        log.info("profileImage : {}", res.getProfileImageUrl());

        return res;
    }
}
