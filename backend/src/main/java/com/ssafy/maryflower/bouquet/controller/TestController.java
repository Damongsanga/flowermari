package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import com.ssafy.maryflower.infrastructure.TestRedisMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final FlowerRepository flowerRepository;
    private final RedisEventPublisher redisEventPublisher;
    // 서버 커넥션 테스트
    @PostMapping("/connection-test")
    private ResponseEntity<String> forTest() {
        return ResponseEntity.ok("Success");
    }

    // DB 커넥션 테스트
    @PostMapping("/db-test")
    private ResponseEntity<String> forDBTest() {

        Optional<Long> test = flowerRepository.findFlowerByName("test");

        return ResponseEntity.ok("success");
    }

    // Redis pub/sub Test
    // Test 완료.
    @PostMapping("/redisTest")
    private ResponseEntity<String> redisTest(){
        redisEventPublisher.TestsendMessage("gogo");

        return ResponseEntity.ok("success");
    }

    // api 사용기록 저장 테스트

    // api 사용 횟수 확인 테스트

    // Top 7 꽃 가져오기 Test

    // 모든 꽃 가져오기 Test -> List에 Flower dto 담자.

    // gpt API Test


}
