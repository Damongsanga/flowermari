package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import com.ssafy.maryflower.bouquet.service.CacheService;
import com.ssafy.maryflower.bouquet.service.DataPublishService;
import com.ssafy.maryflower.bouquet.service.SelectFlowerService;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import com.ssafy.maryflower.infrastructure.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    private final SelectFlowerService selectFlowerService;
    private final FlowerRepository flowerRepository;
    private final RedisEventPublisher redisEventPublisher;
    private final BouquetService bouquetService;
    private final CacheService cacheService;
    private final DataPublishService dataPublishService;
    private final SseEmitters sseEmitters;
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

    @PostMapping("/testgptapi")
    public ResponseEntity<String> testgptapi(){

        String msg= selectFlowerService.chat(selectFlowerService.makePrompt("여자친구","100일 기념일","사랑을 맹세"));
        List<String> flowers=new ArrayList<>();
        flowers.add("수국");
        flowers.add("해바라기");
//        flowers.add("하양 장미");

        String msg2= selectFlowerService.chat(selectFlowerService.makePrompt(flowers));

        System.out.println(msg);
        System.out.println("=================================================");
        System.out.println(msg2);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/publishFlowerDataToAIServerTest")
    public ResponseEntity<String> publishFlowerDataToAIServerTest(){
        List<String> flowers=new ArrayList<>();
        flowers.add("아이리스");
        flowers.add("빨강 장미");
        flowers.add("보라 튤립");
        dataPublishService.publishFlowerDataToAIServer("연인","100일 기념일","영원한 사랑", "reqeustID");
//        dataPublishService.publishFlowerDataToAIServer(flowers,"reqeustID");

        return ResponseEntity.ok("success");
    }

    @PostMapping("/removeSseConnection")
    public ResponseEntity<String> removesseconnection(){
        sseEmitters.removeAllEmitter();
        return ResponseEntity.ok("success");
    }

    private final StringRedisTemplate stringRedisTemplate;
    @PostMapping("/checkAllflowerCache")

    public ResponseEntity<String> checkCache(){
        // 'allFlowers' 캐시에서 'simpleKey []'에 해당하는 데이터 확인
        String cacheKey = "allFlowers::SimpleKey []"; // 캐시 이름과 실제 키 값을 올바르게 조합
        boolean exists = stringRedisTemplate.hasKey(cacheKey);
        System.out.println("Cache exists: " + exists);
        if (exists) {
            // 캐시에서 데이터 가져오기
            String cachedData = stringRedisTemplate.opsForValue().get(cacheKey);
            System.out.println("Cached data: " + cachedData);
        }




//        cacheService.getAllFlowers();
//        System.out.println(cacheService.getAllFlowers().size());
//        System.out.println(stringRedisTemplate.hasKey("allFlowers"));
//
        return ResponseEntity.ok("success");
    }

}
