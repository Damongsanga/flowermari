package com.ssafy.maryflower.global.auth.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoOAuthMemberInfoResponse {

    @JsonProperty("id")
    private String oauthId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public String getProfileImageUrl(){
        if (kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getProfileImageUrl();
    }

    public String getNickname(){
        if (kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getNickname();
    }

    @Getter
    public static class KakaoAccount{

        @JsonProperty("profile")
        private Profile profile;

        @Getter
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }

    }

}