package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.response.reGenerateDto;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import com.ssafy.maryflower.bouquet.service.CacheService;
import com.ssafy.maryflower.bouquet.service.DataPublishService;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/bouquet")
@RequiredArgsConstructor
public class BouquetController {

    private final SseEmitters sseEmitters;
    private final BouquetService bouquetService;
    private final CacheService cacheService;
    private final DataPublishService DataPublishService;



    // SSE 통신 엔드포인트
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(){

        Long userId = 1L;
        return sseEmitters.addEmitter(cacheService.cacheRequestIdWithUserId(userId));
    }

    @PostMapping("/text-input")
    public ResponseEntity<String> processSendUserInputToAIServer(@RequestBody UserDataHolder userDataHolder) {

        // 토큰에서 userId 추출.
        Long userId = 1L;

        // api 호출 회수 조회.
        if (bouquetService.checkApiUses(userId) > 5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API 사용 횟수를 초과하였습니다");
        }

        // api 사용로그 저장
        bouquetService.createApiLog(userId);

        // 요청에 대한 requestId 생성
        String requestId = bouquetService.generateRequestID();

        // userId를 key 값으로 requestId 캐시 (regenerate시 requestId를 기억하기 위함)
        cacheService.cacheRequestIdWithUserId(userId, requestId);

        // userData를 담아 놓을 Dto 생성
        userDataHolder.setUserId(userId);

        // userID를 key로 하여 데이터 Redis 캐시에 저장.
        cacheService.cacheUserDataWithUserId(requestId, userDataHolder);

        // API를 통해 꽃다발에 사용할 꽃 추출 후, Redis ch1으로 publish
        DataPublishService.publishFlowerDataToAIServer(userDataHolder.getWhom(), userDataHolder.getSituation(), userDataHolder.getMessage(), requestId);

        System.out.println("여기까지 옴");
        // 200 응답 반환
        return ResponseEntity.ok("success");
    }

    @PostMapping("/re-generate")
    private ResponseEntity<String> processSendUserFlowersToAIServer(@RequestBody List<String> flowers) {

        // 토큰에서 userId 추출.
        Long userId = 1L;

        // api 호출 회수 조회.
        if (bouquetService.checkApiUses(userId) > 5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API 사용 횟수를 초과하였습니다");
        }

        // api 사용로그 저장
        bouquetService.createApiLog(userId);

        // redis 캐시 확인해 requestId 조회.
        String requestId = cacheService.cacheRequestIdWithUserId(userId);

        // Redis ch1으로 publish
        DataPublishService.publishFlowerDataToAIServer(flowers, requestId);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/confirmed")
    public ResponseEntity<String> confirmBouquet() {

        // 토큰에서  userId 추출.
        Long userId = 1l;

        // redis 캐시 확인해 requestId 조회.
        String requestId = cacheService.cacheRequestIdWithUserId(userId);

        // 유저가 입력 했던 Text 정보 저장된 dto 캐시에서 가져오기.
        UserDataHolder userDataHolder = cacheService.cacheUserDataWithUserId(requestId);

        // 꽃다발 url, 사용된 꽃 list 가져와서 DB에 저장.
        if (cacheService.cachereGenerateDto(requestId) != null) {
            // 재생성을 했을 경우
            reGenerateDto regeneratedto = cacheService.cachereGenerateDto(requestId);
            bouquetService.saveBucketData(userDataHolder.getWhom(), userDataHolder.getSituation(), userDataHolder.getMessage()
                    , regeneratedto.getBouquetUrl(), userId, regeneratedto.getUsedFlower());
            // 캐시 데이터 삭제.
            cacheService.deleteRegenerateDtoFromCache(requestId);
        } else {
            // 재생성을 하지 않았을 경우,
            firstGenerateDto firstgeneratedto = cacheService.cachefirstGenerateDto(requestId);
            bouquetService.saveBucketData(userDataHolder.getWhom(), userDataHolder.getSituation(), userDataHolder.getMessage()
                    , firstgeneratedto.getBouquetUrl(), userId, firstgeneratedto.getUsedFlower());
            // 캐시 데이터 삭제.
            cacheService.deleteFirstGenerateDto(requestId);
        }
        // 캐시 데아터 삭제
        cacheService.deleteUserDataHolderDto(requestId);

        // 캐시 데이터 삭제
        cacheService.deleteRequestIdFromCache(userId);

        // sse 통신 제거.
        sseEmitters.removeEmitter(requestId);

        return ResponseEntity.ok("success");
    }




}
