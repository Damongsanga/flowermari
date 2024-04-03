package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.response.reGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class DataPublishService {

    private final BouquetService bouquetService;
    private final SelectFlowerService selectFlowerService;
    private final FlowerRepository flowerRepository;
    private final CacheService cacheService;
    private final RedisEventPublisher redisEventPublisher;


    // 꽃다발 첫 생성시
    @Async
    public void publishFlowerDataToAIServer(String whom,String situation,String message,String requestId){



        String[] flowers= selectFlowerService.chat(selectFlowerService.makePrompt(whom,situation,message)).split(",");

        firstGenerateDto firstgeneratedto=new firstGenerateDto();

        for(int i=0;i<flowers.length;i++){
            log.info("첫 생성 꽃 이름 : {}", flowers[i]);
            if(i%2 ==0 ){
                // mainflower
                flowerRepository.findFlowerByName(flowers[i]).ifPresent(firstgeneratedto.getUsedFlower()::add);
            }else{
                // subflower
                flowerRepository.findFlowerByName(flowers[i]).ifPresent(firstgeneratedto.getRecommendByMeaning()::add);
            }
        }
        for(Long l:firstgeneratedto.getUsedFlower()){
            log.info("사용된 Main 꽃 ID : {}", l);
        }

        for(Long l:firstgeneratedto.getRecommendByMeaning()){
            log.info("사용된 Sub 꽃 ID : {}", l);
        }

        // 인기순 Top 7 꽃 id 리스트에 저징.
        firstgeneratedto.setRecommendByPopularity(flowerRepository.findTopUsedFlowers());

        // 전체 꽃 데이터 리스트에 저장.
        firstgeneratedto.setAllFlowers(cacheService.getAllFlowers());

        // api 사용 횟수 저장.
        firstgeneratedto.setApiUsageCount(bouquetService.checkApiUses(cacheService.cacheUserDataWithUserId(requestId).getUserId()));

        // firstgeneratedto redis cache에 저장.
        cacheService.cachefirstGenerateDto(requestId,firstgeneratedto);
        log.info("꽃다발 생성 로직 AI로 Publish");

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(firstgeneratedto.getUsedFlower(),requestId));

    }

    // 꽃다발 재 생성시
    @Async
    public void publishFlowerDataToAIServer(List<String> userFlowers, String requestId){

        // 메인 꽃 추출
        String[] flowers= selectFlowerService.chat(selectFlowerService.makePrompt(userFlowers))
                .replace(", ",",") .split(",");

        log.info("regenerate test");

        for(String test:flowers){
            log.info("재생성 꽃이름 : {}", test);
        }

        reGenerateDto regenerateDto=new reGenerateDto();

        log.info("front data size : {}", userFlowers.size());

        for(int i=0;i<userFlowers.size();i++) {
            // dto에 mainflower pk 저장
            flowerRepository.findFlowerByName(userFlowers.get(i)).ifPresent(regenerateDto.getUsedFlower()::add);

            if (i < flowers.length) {
                flowerRepository.findFlowerByName(flowers[i]).ifPresent(regenerateDto.getRecommendByMeaning()::add);
            } else {
                regenerateDto.getRecommendByMeaning().add(regenerateDto.getUsedFlower().get(i));
            }

        }

        // 인기순 Top 7 꽃 id 리스트에 저징.
        regenerateDto.setRecommendByPopularity(flowerRepository.findTopUsedFlowers());

        // api 사용 횟수 저장.
        regenerateDto.setApiUsageCount(bouquetService.checkApiUses(cacheService.cacheUserDataWithUserId(requestId).getUserId()));

        // regeneratedto redis cache에 저장.
        cacheService.cachereGenerateDto(requestId,regenerateDto);

        for(Long l:regenerateDto.getRecommendByMeaning()){
            log.info("추천 꽃 id : {}", l);
        }

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(regenerateDto.getUsedFlower(),requestId));

    }


}
