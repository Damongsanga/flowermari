package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.request.UserDataHolder;
import com.ssafy.maryflower.bouquet.data.dto.response.FlowerDto;
import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.entitiy.Flower;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CacheService {

    private final FlowerRepository flowerRepository;

    /*
    Spring Framework의 캐싱 추상화를 사용. 특정 메서드의 반환값을 캐시에 저장.
    @Cacheable -> 메서드 실행 결과를 캐시에 저장.
    같은 메서드 호출시 캐시에서 직접 결과를 반환하여 메서드 실행 시간 단축.

    userDataHolder를 UserDataHolder라는 이름의 캐시에 저장. 캐시 키는 requestId의 값으로 결정.
    메서드 실행 시 스프링에서 먼저 "UserDataHolder"에서 주어진 "requestId"를 키로 하는 캐시 값을 검색.
    만약 캐시에 이미 데이터가 존재 -> 캐시에서 데이터 반환 후 메서드 실행 x.
    캐시에 데이터 존재x -> 캐시에 해당 키의 데이터 없으먄 메서드 실행 후, 반환 값 캐시에 저장.
     */
    @CachePut(value = "UserDataHolder", key="#requestId")
    public UserDataHolder cacheUserDataWithUserId(String requestId, UserDataHolder userDataHolder) {

        return userDataHolder;
    }

    @CachePut(value = "firstGenerateDto", key="#requestId")
    public firstGenerateDto cachefirstGenerateDto(String requestId, firstGenerateDto firstgeneratedto){
        return firstgeneratedto;
    }

    @Cacheable(value = "firstGenerateDto", key="#requestId")
    public firstGenerateDto cachefirstGenerateDto(String requestId){
        return null;
    }
    /*
    Redis Cache에서 먼저 데이터 확인 후, 없으면 DB 조회
     */
    @Cacheable(value = "allFlowers")
    public List<FlowerDto> getAllFlowers(){

        // 엔티티 전체 조회
        List<Flower> flowers= flowerRepository.findAll();

        // 클라이언트로 반환 할 전체 DTO
        List<FlowerDto> flowerDtos=new ArrayList<>();

        for (Flower flower:flowers){
            FlowerDto dto=new FlowerDto(
                    flower.getFlowerId(),
                    flower.getImageUrl(),
                    flower.getKoreanName(),
                    flower.getMeaning(),
                    flower.getImageUrl()
            );
            flowerDtos.add(dto);
        }
        return flowerDtos;
    }



}
