package com.ssafy.maryflower.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/*
 런타임 에러를 상속받아 에러 커스터 마이징
 런타임 에러는 프로그램이 실행중일때 발생 할 수 있는 오류.
 사용 예시
 throw new GlobalException(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없습니다.");
 */
@Getter
public class GlobalException extends RuntimeException{
    private final HttpStatus statusCode;
    private final String errorCode;
    private final String message;

    public GlobalException(ErrorCode errorCode){
        this.statusCode=errorCode.getStatusCode();
        this.errorCode=errorCode.getErrorCode();
        this.message=errorCode.getMessage();
    }
}
