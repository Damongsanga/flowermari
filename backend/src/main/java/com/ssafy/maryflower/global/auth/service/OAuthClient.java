package com.ssafy.maryflower.global.auth.service;

import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthAccessTokenResponse;
import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthMemberInfoResponse;

public interface OAuthClient {
    KakaoOAuthAccessTokenResponse getAccessToken(String code);
    KakaoOAuthMemberInfoResponse getMemberInfo(String accessToken);
}
