package com.ssafy.maryflower.global.config;

import com.ssafy.maryflower.global.*;
import com.ssafy.maryflower.global.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final OAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
//  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  public SecurityConfig(OAuth2UserService oAuth2UserService) {
    this.oAuth2UserService = oAuth2UserService;
  }
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
    return web -> web.ignoring()
        // error endpoint를 열어줘야 함, favicon.ico 추가!
        .requestMatchers("/error", "/favicon.ico");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
//        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(CsrfConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/member", "/member/check", "/api/v1/auth/**", "/oauth2/**").permitAll()
            .requestMatchers("/auth/login", "/auth/reissue", "auth/oauth2/login/*").permitAll()
            // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
            .anyRequest().authenticated())

        .oauth2Login(oauth2Configurer -> oauth2Configurer
//          .loginPage("/login")
            // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
             .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
        )
        // 에러 핸들링
        .exceptionHandling(ex -> {
          ex.authenticationEntryPoint(new jwtAuthenticationEntryPoint())
              .accessDeniedHandler(new jwtAccessDeniedHandler());
        })
        // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        // JwtException 핸들링을 위한 Exception 필터
        .addFilterBefore(new JwtExceptionFilter(), jwtAuthenticationFilter.getClass()); // 토큰 예외 핸들링

//    .oauth2Login(oauth2 -> oauth2
//        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
//        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
//        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
//        .successHandler(oAuth2SuccessHandler)
//    )
    return http.build();
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    return ((request, response, authentication) -> {
      DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

      String id = defaultOAuth2User.getAttributes().get("id").toString();
      String body = """
                    {"id":"%s"}
                    """.formatted(id);

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());

      PrintWriter writer = response.getWriter();
      writer.println(body);
      writer.flush();
    });
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt Encoder 사용
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
