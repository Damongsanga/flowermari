package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import com.ssafy.maryflower.bouquet.service.CacheService;
import com.ssafy.maryflower.bouquet.service.DataPublishService;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/bouquet")
@RequiredArgsConstructor
public class BouquetController {

    private final SseEmitters sseEmitters;
    private final BouquetService bouquetService;
    private final CacheService cacheService;
    private final DataPublishService DataPublishService;

    @GetMapping("/text-input")
    public SseEmitter processSendUserInputToAIServer(@RequestBody UserDataHolder userDataHolder){

        // 토큰에서 userId 추출.
        Long userId=1L;

        // api 호출 회수 조회.
        if(bouquetService.checkApiUses(userId)>5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,"API 사용 횟수를 초과하였습니다");
        }

        // api 사용로그 저장
        bouquetService.createApiLog(userId);

        // 요청에 대한 requestId 생성
        String requestId= bouquetService.generateRequestID();

        // userData를 담아 놓을 Dto 생성
        userDataHolder.setUserId (userId);

        // userID를 key로 하여 데이터 Redis 캐시에 저장.
        cacheService.cacheUserDataWithUserId(requestId, userDataHolder);

        // API를 통해 꽃다발에 사용할 꽃 추출 후, Redis ch1으로 publish
        DataPublishService.publishFlowerDataToAIServer(userDataHolder.getWhom(),userDataHolder.getSituation(),userDataHolder.getMessage(),requestId);

        // sseEmitter 생성 후 반환
        return sseEmitters.addEmitter(requestId);
    }

}
