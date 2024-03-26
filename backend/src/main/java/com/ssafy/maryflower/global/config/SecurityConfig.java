package com.ssafy.maryflower.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.maryflower.global.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //시큐리티 활성화 -> 기본 스프링 필터 체인에 등록
@RequiredArgsConstructor
public class SecurityConfig  {

//  private final PrincipalOauthUserService principalOauthUserService;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final ObjectMapper objectMapper;

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt Encoder 사용
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
    return web -> web.ignoring()
            // error endpoint를 열어줘야 함, favicon.ico 추가!
            .requestMatchers("/error", "/favicon.ico");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(CsrfConfigurer::disable)
            .sessionManagement(sm -> sm
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 테스트를 위해 authorize 기능 잠시 꺼둠
//            .authorizeHttpRequests(authorize -> authorize
//                    // 해당 API에 대해서는 모든 요청을 허가
//                    .requestMatchers("/member", "/member/check").permitAll()
//                    .requestMatchers("/auth/login", "/auth/reissue", "/auth/oauth2/login/*").permitAll()
//
//                    // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
//                    .anyRequest().authenticated())
            .authorizeHttpRequests(authorize -> authorize
                    // 모든 요청을 허가
                    .anyRequest().permitAll())

            .exceptionHandling(ex -> {
              ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                      .accessDeniedHandler(jwtAccessDeniedHandler);
            }).addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            // JwtException 핸들링을 위한 Exception 필터
            .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);

    return http.build();
  }
}
