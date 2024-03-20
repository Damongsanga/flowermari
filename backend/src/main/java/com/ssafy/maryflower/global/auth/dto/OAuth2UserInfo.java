package com.ssafy.maryflower.global.auth.dto;

import com.ssafy.maryflower.member.data.entity.Member;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
    String name,
    String email,
    String profile
) {

  public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
    return switch (registrationId) { // registration id별로 userInfo 생성
      case "kakao" -> ofKakao(attributes);
      default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
    };
  }

  private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    return OAuth2UserInfo.builder()
        .name((String) profile.get("nickname"))
        .profile((String) profile.get("profile_image_url"))
        .build();
  }

  public Member toEntity() {
    return Member.builder()
        .name(name)
        .profile(profile)
        .memberKey(KeyGenerator.generateKey())
        .role(Role.USER)
        .build();
  }
}
