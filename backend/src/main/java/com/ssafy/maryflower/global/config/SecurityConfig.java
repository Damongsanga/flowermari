package com.ssafy.maryflower.global.config;

import com.ssafy.maryflower.global.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  private final OAuth2UserService oAuth2UserService;

  public SecurityConfig(OAuth2UserService oAuth2UserService) {
    this.oAuth2UserService = oAuth2UserService;
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf().disable();
//    http.authorizeHttpRequests(config -> config.anyRequest().permitAll());
//    http.oauth2Login(oauth2Configurer -> oauth2Configurer
//        .loginPage("/login")
//        .successHandler(successHandler())
//        .userInfoEndpoint()
//        .userService(oAuth2UserService));
//
//    return http.build();
//  }
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/member", "/member/check", "/api/v1/auth/**", "/oauth2/**").permitAll()
            .requestMatchers("/auth/login", "/auth/reissue", "auth/oauth2/login/*").permitAll()
            // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
            .anyRequest().authenticated())

        .oauth2Login(oauth2Configurer -> oauth2Configurer
          .loginPage("/login")
          .successHandler(successHandler())
          .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
        );
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
}
