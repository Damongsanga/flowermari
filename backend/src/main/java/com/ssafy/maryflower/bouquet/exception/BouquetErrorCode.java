package com.ssafy.maryflower.bouquet.exception;

import com.ssafy.maryflower.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// 여기서 오버라이딩을 안해도 되는 이유. @Getter로 인자 앞에 get을 붙이면  ErrorCode의 메서드와 이름 일치.
public enum BouquetErrorCode implements ErrorCode {

    API_USAGE_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "BOUQUET_01", "API 사용 횟수를 초과하였습니다."),
    INCOMPLETE_INPUT_DATA(HttpStatus.BAD_REQUEST, "BOUQUET_02", "필수 입력 데이터가 누락되었습니다."),
    INSUFFICIENT_FLOWERS_RECOMMENDED(HttpStatus.BAD_REQUEST, "BOUQUET_03", "추천된 꽃의 수가 충분하지 않습니다."),
    INVALID_MEMBER_ID(HttpStatus.NOT_FOUND, "BOUQUET_04", "부적절한 멤버 Id 입니다."),
    INVALID_FLOWER_ID(HttpStatus.NOT_FOUND, "BOUQUET_05", "적절하지 않은 꽃 Id 입니다."),
    REQUEST_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "BOUQUET_06", "캐시에서 요청 ID를 찾을 수 없습니다.");

    private final HttpStatus statusCode;
    private final String errorCode;
    private final String message;


}
