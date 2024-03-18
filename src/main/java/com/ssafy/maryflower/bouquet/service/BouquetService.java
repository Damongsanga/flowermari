package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;

import com.ssafy.maryflower.bouquet.data.dto.response.BouquetDetailDto;
import com.ssafy.maryflower.bouquet.data.dto.response.FlowerDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.BouquetUrlTransferDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import com.ssafy.maryflower.bouquet.data.entitiy.Flower;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BouquetService {

    private final FlowerCombinationService flowerCombinationService;
    private final RedisEventPublisher redisEventPublisher;
    private final FlowerRepository flowerRepository;
    // random uuid 생성을 통해 reqyestId 설정
    public String generateRequestID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }


    /* value로 캐시 이름 지정, key로 캐시 키 지정.
     먼저 캐시에서 requestId를 key로 하는 데이터를 조회해보고 있으면 그걸 반환 없으면 return되는 값 즉 주어진
     UserDataHolder 를 캐시에 저장
    */
    @Cacheable(value = "UserDataHolder", key="#requestId")
    public UserDataHolder cacheUserDataWithUserId(String requestId,UserDataHolder userDataHolder) {

        return userDataHolder;
    }


    // chatgpt api 호출 및 Redis Publish 로직 구현
    @Async
    public void publishFlowerDataToAIServer(String whom,String situation,String message,String reqeustId){

        // 사용자가 입력한 text로 prompt 생성
        String prompt= generatePrompt(whom,situation,message);

        // gpt를 통해
        String[] flowers=flowerCombinationService.callChatGptApi(prompt).split(",");

        // main flower 저장.
        List<String> mainflowerName = new ArrayList<>();

        // sub flower 저장.
        List<String> subFlowerMeaning= new ArrayList<>();

        for(int i=0;i<flowers.length;i++){

            if(i%2 ==0 ){
                // mainflower
                mainflowerName.add(flowers[i]);
            }else{
                // subflower
                subFlowerMeaning.add(flowers[i]);
            }
        }
        // main flower
        List<FlowerDto> mainflower=new ArrayList<>();

        // sub flower(꽃말 기준)
        List<FlowerDto> recommendByMeaning=new ArrayList<>();

        // sub flower(인기도 기준) Top 7 가져오기.
        List<FlowerDto> recommendByPopularity=new ArrayList<>();

        // main flower 정보 캐시, DB 조회 후 가져오기
        mainflower=getFlowerInfoByName(mainflowerName);

        // sub flower(꽃말 기준) 정보 캐시, DB 조회 가져오기
        recommendByMeaning=getFlowerInfoByName(subFlowerMeaning);

        // sub flower(인기도 기준) 정보 캐시, DB 조회 가져오기.
        recommendByPopularity=getFlowerInfoByPopularity();

        // 모든 꽃 데이터 캐시, DB 조회하여 가져오기.
        

        // main flower 정보, 요청 아이디 dto로 Redis publish
        redisEventPublisher.sendMessage(new FlowersTransferDto(mainflowerName,reqeustId));

    }

    public List<FlowerDto> getFlowerInfoByName(List<String> flowerNames){
        List<FlowerDto> flowers=new ArrayList<>();

        for(String name:flowerNames){
            flowers.add(cacheFlowerDtoByName(name));
        }
        return flowers;
    }

    public List<FlowerDto> getFlowerInfoByPopularity(){
        List<Flower> Entity=flowerRepository.findTopUsedFlowers();
        List<FlowerDto> flowers=new ArrayList<>();
        for(Flower F:Entity){
            flowers.add(new FlowerDto(F.getImageUrl(),F.getKoreanName(),F.getMeaning(),F.getColor()));
        }
        return flowers;
    }

    @Cacheable(value = "FlowerDto", key="#flowerName")
    public FlowerDto cacheFlowerDtoByName(String flowerName){
        Flower entity=flowerRepository.findByKoreanName(flowerName);
        if(entity!=null){
            return new FlowerDto(entity.getImageUrl(), entity.getKoreanName(), entity.getMeaning(), entity.getColor());
        }
        return null;
    }

    public String generatePrompt(String whom, String situation, String message){
        return "";
    }
}
