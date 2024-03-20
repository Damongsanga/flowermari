package com.ssafy.maryflower.global.auth.data;

public interface OAuth2MemberInfo {
  String getKakaoId(); //공급자 아이디 ex) google, facebook
  String getNickname(); //사용자 이름 ex) 홍길동
}
