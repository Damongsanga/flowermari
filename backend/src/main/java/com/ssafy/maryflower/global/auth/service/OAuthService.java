package com.ssafy.maryflower.global.auth.service;


import com.ssafy.maryflower.global.auth.AES128Util;
import com.ssafy.maryflower.global.auth.JwtTokenProvider;
import com.ssafy.maryflower.global.auth.data.UserRole;
import com.ssafy.maryflower.global.auth.data.dto.JwtToken;
import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthMemberInfoResponse;
import com.ssafy.maryflower.global.auth.data.dto.LoginDto;
import com.ssafy.maryflower.global.auth.data.dto.KakaoOAuthAccessTokenResponse;
import com.ssafy.maryflower.member.data.entity.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthClient kakaoOAuthClient;
    private final AES128Util aes128Util;


    @Value("spring.security.oauth2.client.registration.password-salt")
    private String salt;

    public String reissueAccessToken(String encryptedRefreshToken) {
        // 유저가 제공한 refreshToken이 있는지 확인
        if (encryptedRefreshToken == null) throw new RuntimeException("쿠키에 refreshToken이 존재하지 않습니다");
        String refreshToken = aes128Util.decryptAes(encryptedRefreshToken);
        // userId 정보를 가져와서 redis에 있는 refreshtoken과 같은지 확인
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String kakaoId = claims.getSubject();
//
//        String redisRefreshToken = jwtRedisRepository.find(KeyUtil.getRefreshTokenKey(memberId));
//        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) throw new RuntimeException("refreshToken이 유효하지 않습니다");
        // 같다면 refreshToken을 활용하여 새로운 accessToken을 발급
        return jwtTokenProvider.generateAccessToken(kakaoId, claims.get("auth").toString());
    }

    @Transactional
    public LoginDto kakaoOAuthClient(String code) {
        KakaoOAuthMemberInfoResponse res = getKakaoUserInfo(code);
        String memberId = res.getOauthId();
        log.info("oauthId : {}", memberId);
        createIfNewMember(memberId, res);
        return login(memberId);
    }

    private LoginDto login(String kakaoId) {
        JwtToken jwtToken = makeJwtToken(kakaoId);
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("멤버가 없습니다."));

        return LoginDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .jwtToken(jwtToken)
                .build();
    }

    private KakaoOAuthMemberInfoResponse getKakaoUserInfo(String code) {
        log.info("code : {}", code);
        try {
            KakaoOAuthAccessTokenResponse tokenResponse = kakaoOAuthClient.getAccessToken(code);
            return kakaoOAuthClient.getMemberInfo(tokenResponse.getAccessToken());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new RuntimeException("소셜 로그인 인증 에러");
        }
    }

    private JwtToken makeJwtToken(String memberId) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, memberId+salt);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    private void createIfNewMember(String kakaoId, KakaoOAuthMemberInfoResponse res) {
        if (!memberRepository.existsByKakaoId(kakaoId)) {
            Member member =
                    Member.builder()
                            .kakaoId(kakaoId)
                            .nickname(res.getNickname())
                            .password(passwordEncoder.encode(kakaoId + salt))
                            .profileImage(res.getProfileImageUrl())
                            .role(UserRole.USER).build();
            memberRepository.save(member);
        }
    }


}
