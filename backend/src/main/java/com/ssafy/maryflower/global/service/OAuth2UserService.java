package com.ssafy.maryflower.global.service;
import com.ssafy.maryflower.global.auth.dto.OAuth2UserInfo;
import com.ssafy.maryflower.global.auth.dto.PrincipalDetails;
import com.ssafy.maryflower.member.data.entity.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

//  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    // 1. 유저 정보(attributes) 가져오기
    Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

    // 2. resistrationId 가져오기 (third-party id)
    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    // 3. userNameAttributeName 가져오기
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    // 4. 유저 정보 dto 생성
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

    // 5. 회원가입 및 로그인
    Member member = getOrSave(oAuth2UserInfo);

    // 6. OAuth2User로 반환
    return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
//    OAuth2User oAuth2User = super.loadUser(userRequest);
//
//    // Role generate
//    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
//
//    // nameAttributeKey
//    String userNameAttributeName = userRequest.getClientRegistration()
//        .getProviderDetails()
//        .getUserInfoEndpoint()
//        .getUserNameAttributeName();
//
//    // DB 저장 로직
//    return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
  }
  private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
    Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
        .orElseGet(oAuth2UserInfo::toEntity);
    return memberRepository.save(member);
  }
}
