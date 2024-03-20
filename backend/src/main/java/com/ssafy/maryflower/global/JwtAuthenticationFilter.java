package com.ssafy.maryflower.global;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    // 1. Request Header에서 JWT 토큰 추출
    String token = resolveToken(request);
    // reissue일 경우는 토큰 검사 X
    if (token != null && jwtTokenProvider.validateToken(token)) {
      // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    String accessToken = resolveToken(request);
//
//    // accessToken 검증
//    if (jwtTokenProvider.validateToken(accessToken)) {
//      setAuthentication(accessToken);
//    } else {
//      // 만료되었을 경우 accessToken 재발급
//      String reissueAccessToken = jwtTokenProvider.reissueAccessToken(accessToken);
//
//      if (StringUtils.hasText(reissueAccessToken)) {
//        setAuthentication(reissueAccessToken);
//
//        // 재발급된 accessToken 다시 전달
//        response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + reissueAccessToken);
//      }
    filterChain.doFilter(request, response);
  }

  private void setAuthentication(String accessToken) {
    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String resolveToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
      return token.substring(7);
    }
    return null;
  }

//  private String resolveToken(HttpServletRequest request) {
//    String token = request.getHeader(AUTHORIZATION);
//    if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
//      return null;
//    }
//    return token.substring(TokenKey.TOKEN_PREFIX.length());
//  }
}
