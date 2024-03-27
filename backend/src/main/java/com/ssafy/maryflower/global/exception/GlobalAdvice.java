package com.ssafy.maryflower.global.exception;

import com.ssafy.maryflower.global.exception.dto.GlobalExceptionResponse;
import com.ssafy.maryflower.global.exception.dto.ValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// @RestControllerAdvice ->  전역 예외 핸들러 정의.
@RestControllerAdvice
public class GlobalAdvice {

    /*
    예외 처리 메서드
    - GlobalExceptionResponse.toResponse(ex)를 활용해 예외 정보를 담은 ResponseEntitiy 객체로 변환.
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> illegalArgumentResponse(GlobalException ex) {
        return GlobalExceptionResponse.toResponse(ex);
    }

    /*
     메서드 매개변수 검증 실패 예외 처리. 유효하지 않은 요청 데이터에 대한 사용자 친화적인 메시지 반환.
     컨트롤러단에서 @Valid 유효성 검사를 통과하지 못 했을 떄 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> validationHandlerException(MethodArgumentNotValidException e) {
        return ValidationExceptionResponse.toResponse(e);
    }

    /*
     IllegalArgumentException 예외 처리
      - 잘못된 인자로 인한 예외.
      - 범위를 벗어난 값.
      - 부적합한 형식의 값.
     Http 상태 코드 BAD_REQUEST 로 응답.

     자바의 어떤 메서드에 잘못된 인자가 전달 되었을 떄 발생
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handler(IllegalArgumentException e) {
        return e.getMessage();
    }
}
