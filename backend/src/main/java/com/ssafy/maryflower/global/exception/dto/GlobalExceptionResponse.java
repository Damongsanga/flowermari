package com.ssafy.maryflower.global.exception.dto;

import com.ssafy.maryflower.global.exception.GlobalException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;


@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GlobalExceptionResponse {

    // 클라이언트에게 전달할 메세지 저장 필드.
    private String message;
    // 발생한 예외에 대한 고유한 에러 코드 저장 필드.
    private String errorCode;

    /*
     전달받은 GlobalException 객체 기반으로,
     ResponseEntity<GlobalExceptionResponse> 객체 생성하여 반환하는 정적 메서드.
     이 메서드는 예외 처리 로직에서 발생하며, 예외 발생 시 클라이언트에게 전달 할 HTTP 응답을 구성.
     */
    public static ResponseEntity<GlobalExceptionResponse> toResponse(GlobalException e){
        // ResponseEntity의 상태 코드를 설정하는데 GlobalException에서 제공하는 상태코드 사용.
        // 이를 통해 적절한 HTTP 응답 가능.
        return ResponseEntity
                .status(e.getStatusCode()) // 예외 객체에서 상태 코드를 가져와 응답의 상태 코드 설정.
                .body(GlobalExceptionResponse.builder() // GlobalExceptionResponse 객체를 빌더 패턴을 이용해 생성.
                        .errorCode(e.getErrorCode()) // 예외 객체에서 에러 코드를 가져와 설정
                        .message(e.getMessage()) // 예외 객체에서 메시지를 가져와 설정
                        .build() // 빌더 패턴을 이용해 생성된 객체를 응답 본문으로 설정
                );
    }
}
