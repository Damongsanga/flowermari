package com.ssafy.maryflower.global.auth.service;

import com.ssafy.maryflower.global.PrincipalDetails;
import com.ssafy.maryflower.global.auth.data.KakaoMemberInfo;
import com.ssafy.maryflower.global.auth.data.OAuth2MemberInfo;
import com.ssafy.maryflower.member.data.entity.Member;

import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Optional;
@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {
//  private final BCryptPasswordEncoder encoder;
  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    OAuth2MemberInfo memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
    System.out.println(oAuth2User.getAttributes());
    System.out.println(userRequest.getClientRegistration().getRegistrationId());

//    String registrationId = userRequest.getClientRegistration().getRegistrationId();
//    System.out.println("registrationId = " + registrationId);
//    if (registrationId.equals("kakao")) {
//      memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
//    } else {
//      System.out.println("로그인 실패");
//    }
    String kakaoId = memberInfo.getKakaoId();
    String nickname = memberInfo.getNickname();
    String username = kakaoId + "_" + nickname; //중복이 발생하지 않도록 provider와 providerId를 조합
//    String role = "ROLE_ADMIN"; //일반 유저
    System.out.println(oAuth2User.getAttributes());
    Optional<Member> findMember = memberRepository.findByKakaoId(kakaoId);
    Member member=null;
    if (findMember.isEmpty()) { //찾지 못했다면
      member = Member.builder()
          .name(username)
          .nickname(nickname)
          .kakaoId(kakaoId).build();
      memberRepository.save(member);
    }
    else{
      member=findMember.get();
    }
    return new PrincipalDetails(member, oAuth2User.getAttributes());
  }
}
