package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/bouquet")
@RequiredArgsConstructor
public class BouquetController {

    private final SseEmitters sseEmitters;
    private final BouquetService bouquetService;


    @PostMapping("/text-input")
    public SseEmitter processSendUserInputToAIServer(@RequestBody UserDataHolder userDataHolder){

        // 토큰에서 userId 추출.
        Integer userId=1;

        // 요청에 대한 requestId 생성
        String requestId= bouquetService.generateRequestID();

        // userData를 담아 놓을 Dto 생성
        userDataHolder.setUserId (userId);

        // userID를 key로 하여 데이터 Redis 캐시에 저장.
        bouquetService.cacheUserDataWithUserId(requestId, userDataHolder);

        // API를 통해 꽃다발에 사용할 꽃 추출 후, Redis ch1으로 publish
        bouquetService.publishFlowerDataToAIServer(userDataHolder.getWhom(),userDataHolder.getSituation(),userDataHolder.getMessage(),requestId);


        // sseEmitter 생성 후 반환
        return sseEmitters.addEmitter(requestId);
    }

}
