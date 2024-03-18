package com.ssafy.maryflower.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.maryflower.bouquet.data.dto.transfer.BouquetUrlTransferDto;
import com.ssafy.maryflower.bouquet.service.BouquetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@RequiredArgsConstructor
public class RedisMessageListener implements MessageListener {

    BouquetService bouquetService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            Jackson2JsonRedisSerializer<BouquetUrlTransferDto> serializer = new Jackson2JsonRedisSerializer<>(BouquetUrlTransferDto.class);
            BouquetUrlTransferDto BouquetUrlTransferDto = serializer.deserialize(message.getBody());
//            bouquetService.makeBouquetDetailDto(BouquetUrlTransferDto);
        }catch(Exception e){
            // 추후 에러처리 합시다
        }

    }
}
