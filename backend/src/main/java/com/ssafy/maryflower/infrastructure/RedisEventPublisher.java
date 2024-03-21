package com.ssafy.maryflower.infrastructure;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void sendMessage(FlowersTransferDto flowersTransferDto){
        redisTemplate.convertAndSend("ch1", flowersTransferDto);
    }

    public void TestsendMessage(String str){
        redisTemplate.convertAndSend("test", str);
    }
}
