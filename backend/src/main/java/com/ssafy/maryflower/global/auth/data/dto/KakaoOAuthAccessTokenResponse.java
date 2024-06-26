package com.ssafy.maryflower.global.auth.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoOAuthAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
