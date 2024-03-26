package com.ssafy.maryflower.global.auth.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private Long memberId;
    private String profileImage;
    private String accessToken;

    public LoginResponseDto(LoginDto dto){
        this.memberId = dto.getMemberId();
        this.profileImage = dto.getProfileImage();
        this.accessToken = dto.getJwtToken().getAccessToken();
    }
}
