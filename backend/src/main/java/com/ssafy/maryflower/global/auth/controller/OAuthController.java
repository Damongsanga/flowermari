package com.ssafy.maryflower.global.auth.controller;

import com.ssafy.maryflower.global.auth.data.dto.LoginDto;
import com.ssafy.maryflower.global.auth.data.dto.LoginResponseDto;
import com.ssafy.maryflower.global.auth.service.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthService oAuthService;

  @Value("${spring.jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  @GetMapping("login/kakao")
  public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code){
    LoginDto dto = oAuthService.kakaoOAuthClient(code);
    HttpHeaders headers = getHeadersWithCookie(dto.getJwtToken().getRefreshToken());
    LoginResponseDto res = new LoginResponseDto(dto);
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  @GetMapping("/reissue")
  public ResponseEntity<String> reissue(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    if (cookies == null) throw new RuntimeException("쿠키가 존재하지 않습니다");

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refreshToken")){
        String encryptedRefreshToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
        String newAccessToken = oAuthService.reissueAccessToken(encryptedRefreshToken);
        return ResponseEntity.ok(newAccessToken);
      }
    }
    return new ResponseEntity<String>("필요한 쿠키가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
  }

  private HttpHeaders getHeadersWithCookie(String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .maxAge(refreshTokenValidityInSeconds)
            .path("/")
            .secure(true)
            .sameSite("None")
            .httpOnly(true)
            .build();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Set-Cookie", cookie.toString());
    return headers;
  }

}
