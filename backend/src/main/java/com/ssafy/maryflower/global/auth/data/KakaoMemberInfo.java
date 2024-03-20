package com.ssafy.maryflower.global.auth.data;

import java.util.Map;

public class KakaoMemberInfo implements OAuth2MemberInfo{
  private Map<String, Object> attributes;
  private Map<String, Object> kakaoAccountAttributes;
  private Map<String, Object> profileAttributes;

  public KakaoMemberInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    this.profileAttributes = (Map<String, Object>) attributes.get("profile");

  }


  @Override
  public String getKakaoId() {
    return attributes.get("id").toString();
  }

//  @Override
//  public String getProvider() {
//    return "kakao";
//  }

  @Override
  public String getNickname() {
    return kakaoAccountAttributes.get("nickname").toString();
  }
}
