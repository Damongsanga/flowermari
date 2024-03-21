package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.response.reGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataPublishService {

    private final BouquetService bouquetService;
    private final FlowerCombinationService flowerCombinationService;
    private final FlowerRepository flowerRepository;
    private final CacheService cacheService;
    private final RedisEventPublisher redisEventPublisher;


    // 꽃다발 첫 생성시
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

        // api 사용 횟수 저장.
        firstgeneratedto.setApiUsageCount(bouquetService.checkApiUses(cacheService.cacheUserDataWithUserId(requestId).getUserId()));

        // firstgeneratedto redis cache에 저장.
        cacheService.cachefirstGenerateDto(requestId,firstgeneratedto);

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(firstgeneratedto.getUsedFlower(),requestId));

    }

    // 꽃다발 재 생성시
    @Async
    public void publishFlowerDataToAIServer(List<String> userFlowers, String requestId){

        // 사용자가 선택한 꽃 데이터로 prompt 생성
        String prompt= bouquetService.generatePrompt(userFlowers);

        // gpt를 통해 추천 꽃 추출(꽃말 기준)
        String[] flowers=flowerCombinationService.callChatGptApi(prompt).split(",");

        reGenerateDto regenerateDto=new reGenerateDto();

        for(int i=0;i<userFlowers.size();i++){
            // dto에 mainflower pk 저장
            flowerRepository.findFlowerByName(userFlowers.get(i)).ifPresent(regenerateDto.getUsedFlower()::add);

            // dto에 꽃말기준 추천 꽃 pk 저장
            flowerRepository.findFlowerByName(flowers[i]).ifPresent(regenerateDto.getRecommendByMeaning()::add);
        }

        // 인기순 Top 7 꽃 id 리스트에 저징.
        regenerateDto.setRecommendByPopularity(flowerRepository.findTopUsedFlowers());

        // api 사용 횟수 저장.
        regenerateDto.setApiUsageCount(bouquetService.checkApiUses(cacheService.cacheUserDataWithUserId(requestId).getUserId()));

        // regeneratedto redis cache에 저장.
        cacheService.cachereGenerateDto(requestId,regenerateDto);

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(regenerateDto.getUsedFlower(),requestId));

    }


}
