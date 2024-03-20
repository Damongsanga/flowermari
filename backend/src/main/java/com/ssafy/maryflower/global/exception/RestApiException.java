package com.ssafy.maryflower.global.exception;

import com.ssafy.maryflower.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException {
  private final ErrorCode errorCode;
  private final String message;

  public RestApiException(ErrorCode errorCode){
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }
}
