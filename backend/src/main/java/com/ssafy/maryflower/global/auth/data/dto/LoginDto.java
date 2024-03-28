package com.ssafy.maryflower.global.auth.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private JwtToken jwtToken;

}
