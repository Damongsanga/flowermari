package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import com.ssafy.maryflower.bouquet.service.CacheService;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final FlowerRepository flowerRepository;
    private final RedisEventPublisher redisEventPublisher;
    private final BouquetService bouquetService;
    private final CacheService cacheService;

    // 서버 커넥션 테스트
    @PostMapping("/connection-test")
    public ResponseEntity<String> forTest() {
        return ResponseEntity.ok("Success");
    }

    // DB 커넥션 테스트
    @PostMapping("/db-test")
    public ResponseEntity<String> forDBTest() {

        Optional<Long> test = flowerRepository.findFlowerByName("test");

        return ResponseEntity.ok("success");
    }

    // Redis pub/sub Test
    // Test 완료.
    @PostMapping("/redisTest")
    public ResponseEntity<String> redisTest(){
        redisEventPublisher.TestsendMessage("gogo");

        return ResponseEntity.ok("success");
    }

    @PostMapping("/")
    public ResponseEntity<String> example(){

        return ResponseEntity.ok("success");
    }
    // api 사용기록 저장 테스트
    // Test 완료
    @PostMapping("/apiUseStoreTest")
    public ResponseEntity<String> apiUseStoreTest(){
        bouquetService.createApiLog(1L);
        return ResponseEntity.ok("success");
    }

    // api 사용 횟수 확인 테스트
    @PostMapping("/apiUserCountTest")
    public ResponseEntity<String> apiUserCountTest(){
        System.out.println(bouquetService.checkApiUses(1L));
        return ResponseEntity.ok("success");
    }

    // 꽃다발 생성 Test 메서드
    @PostMapping("/saveBucketDataTest")
    public ResponseEntity<String> saveBucketDataTest(){
        List<Long> testList=new ArrayList<>();
        testList.add(2L);
        testList.add(4L);
        testList.add(7L);

        bouquetService.saveBucketData("여자친구", "기념일","축하","testUrl",1L,testList);

        return ResponseEntity.ok("success");
    }

    // 꽃 이름으로 꽃 가져오기.
    @PostMapping("/findFlowerByNameTest")
    public ResponseEntity<String> findFlowerByNameTest(){

        System.out.println(flowerRepository.findFlowerByName("보라 튤립"));
        return ResponseEntity.ok("success");
    }
    // Top 7 꽃 추출
    @PostMapping("/findTopUsedFlowersTest")
    public ResponseEntity<String> findTopUsedFlowersTest(){
        List<Long> testList=flowerRepository.findTopUsedFlowers();
        for(Long te:testList){
            System.out.print(te+" ");
        }

        return ResponseEntity.ok("success");
    }

    // redis 캐시 Test
    @PostMapping("/redisCacheTest")
    public ResponseEntity<String> redisCacheTest(){
        UserDataHolder userDataHolderforTest=new UserDataHolder("test1","test2",
        "test3",1L);
        cacheService.cacheUserDataWithUserId("requestId",userDataHolderforTest);

        System.out.println(cacheService.cacheUserDataWithUserId("requestId").getWhom());
        cacheService.deleteUserDataHolderDto("requestId");
        System.out.println(cacheService.cacheUserDataWithUserId("requestId")==null);
        return ResponseEntity.ok("success");
    }

}
