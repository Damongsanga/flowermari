package com.ssafy.maryflower.global.auth.service;

import com.ssafy.maryflower.member.data.entity.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        return memberRepository.findByKakaoId(kakaoId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new RuntimeException("멤버 없음. 추후 custum error 수정 필요"));
    }

    // 멤버 정보를 UserDetail 객체로 변경하여 return
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getKakaoId())
                .password(member.getPassword())
                .roles(new String[]{member.getRole().toString()})
                .build();
    }
}
