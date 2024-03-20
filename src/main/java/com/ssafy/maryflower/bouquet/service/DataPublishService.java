package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.BouquetUrlTransferDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DataPublishService {

    private final BouquetService bouquetService;
    private final FlowerCombinationService flowerCombinationService;
    private final FlowerRepository flowerRepository;
    private final CacheService cacheService;
    private final RedisEventPublisher redisEventPublisher;

    @Async
    public void publishFlowerDataToAIServer(String whom,String situation,String message,String requestId){

        // 사용자가 입력한 text로 prompt 생성
        String prompt= bouquetService.generatePrompt(whom,situation,message);

        // gpt를 통해
        String[] flowers=flowerCombinationService.callChatGptApi(prompt).split(",");

        firstGenerateDto firstgeneratedto=new firstGenerateDto();

        for(int i=0;i<flowers.length;i++){

            if(i%2 ==0 ){
                // mainflower
                flowerRepository.findFlowerByName(flowers[i]).ifPresent(firstgeneratedto.getUsedFlower()::add);
            }else{
                // subflower
                flowerRepository.findFlowerByName(flowers[i]).ifPresent(firstgeneratedto.getRecommendByMeaning()::add);
            }
        }

        // 인기순 Top 7 꽃 id 리스트에 저징.
        firstgeneratedto.setRecommendByPopularity(flowerRepository.findTopUsedFlowers());

        // 전체 꽃 데이터 리스트에 저장.
        firstgeneratedto.setAllFlowers(cacheService.getAllFlowers());

        // firstgeneratedto redis cache에 저장.
        cacheService.cachefirstGenerateDto(requestId,firstgeneratedto);

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(firstgeneratedto.getUsedFlower(),requestId));

    }



}
