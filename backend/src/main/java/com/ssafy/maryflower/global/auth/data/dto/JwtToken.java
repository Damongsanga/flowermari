package com.ssafy.maryflower.global.auth.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@AllArgsConstructor
@Getter
public class JwtToken{
    private String grantType;
    private String accessToken;
    private String refreshToken;
}