package com.ssafy.maryflower.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    // Http 상태 코드 반환.
    HttpStatus getStatusCode();

    // 메서드 내부 오류 코드.
    String getErrorCode();

    // 사용자에게 보여줄 오류 메시지.
    String getMessage();
}
