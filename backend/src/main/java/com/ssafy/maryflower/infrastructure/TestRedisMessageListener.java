package com.ssafy.maryflower.infrastructure;

import com.ssafy.maryflower.bouquet.data.dto.transfer.BouquetUrlTransferDto;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

// Test 용
@Service
public class TestRedisMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(String.class);
            String str = serializer.deserialize(message.getBody());
            System.out.println(str);
        }catch(Exception e){
            // 추후 에러처리 합시다
        }
    }
}
