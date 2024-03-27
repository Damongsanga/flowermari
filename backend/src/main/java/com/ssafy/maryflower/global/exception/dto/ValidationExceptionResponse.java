package com.ssafy.maryflower.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

/*
유효성 검사 -> 클라이언트로부터 받은 HTTP 요청의 본문을
컨트롤러의 메서드 파라미터로 매핑하는 과정에서 해당 파라미터에 @Valid or @Validated 어노테이션이 붙어 있고,
이 어노테이션에 의해 지정된 유효성 검사 규칙을 만족하지 못했을 때 MethodArgumentNotValidException 발생.
 */
@NoArgsConstructor
@AllArgsConstructor
// getter, setter, toSting, equals, hashcode 메서드 생성을 한번에 해줌
// 주로 단순한 데이터 운반 객체 DTO에 사용.
@Data
@Builder
public class ValidationExceptionResponse {
    // 클라이언트에게 전달할 유효성 검증 실패 메시지를 저장하는 필드.
    private String message;

    // MethodArgumentNotValidException 예외를 받아, 유효성 검증 실패에 대한 정보를 담은
    // ResponseEntity<ValidationExceptionResponse> 객체를 생성하여 반환하는 정적 메서드.
    public static ResponseEntity<ValidationExceptionResponse> toResponse(MethodArgumentNotValidException e) {
        /*
            메서드 유효성 검사 실패 시 발생하는 MethodArgumentNotValidException을 인자로 받음
         */
        return ResponseEntity
                .status(e.getStatusCode()) // HttpStatus.BAD_REQUEST
                .body(ValidationExceptionResponse.builder()
                        /*
                        에러 메시지를 응답 본문에 저장.
                        Objects.requireNonNull()-> 전달된 객체(e.getBindingResult().getFieldError())가 null이 아닌지 확인.
                         */
                        .message(Objects.requireNonNull(
                                e.getBindingResult().getFieldError()
                        ).getDefaultMessage())

                        .build()
                );
    }

}
