package com.ssafy.maryflower.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode{
  NOT_VALID_USER(HttpStatus.UNAUTHORIZED, "사용자 권한이 유효하지 않습니다"),
  WRONG_ACCESS_WITHOUT_AUTHORIZATION(HttpStatus.FORBIDDEN, "비정상적인 접근입니다");

  private final HttpStatus httpStatus;
  private final String message;
}
