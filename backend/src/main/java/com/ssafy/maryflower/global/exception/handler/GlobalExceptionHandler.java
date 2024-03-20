package com.ssafy.maryflower.global.exception.handler;

import com.ssafy.maryflower.global.exception.RestApiException;
import com.ssafy.maryflower.global.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // 커스텀된 에러 리턴을 위한 메서드
  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<Object> handleCustomArgument(RestApiException e) {
    log.warn("RestApiException : {}", e.getMessage());
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode, e.getMessage());
  }
}
