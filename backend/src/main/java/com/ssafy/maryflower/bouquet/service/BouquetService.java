package com.ssafy.maryflower.bouquet.service;


import com.ssafy.maryflower.bouquet.data.entitiy.*;
import com.ssafy.maryflower.bouquet.data.repository.*;
import com.ssafy.maryflower.member.data.entitiy.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BouquetService {
    private final MemberRepository memberRepository;
    private final ApiLogRepository apiLogRepository;
    private final BouquetRepository bouquetRepository;
    private final MemberbouquetRepository memberbouquetRepository;
    private final FlowerRepository flowerRepository;
    private final FlowerbouquetRepository flowerbouquetRepository;
    // random uuid 생성을 통해 reqyestId 설정
    public String generateRequestID(){
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }

    // api 사용기록 저장.
    @Transactional
    public void createApiLog(Long userId){
        // 멤버 조회.
        Member member=memberRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("부적절한 ID: " + userId));

        // Api 로그 생성
        ApiLog apiLog=new ApiLog();

        apiLog.setMember(member);

        apiLogRepository.save(apiLog);
    }

    // 하루동안 api 호출 회수 확인.
    public Integer checkApiUses(Long userid){
        LocalDateTime fromDateTime=LocalDateTime.now().minusDays(1);
        return apiLogRepository.numberOfApiUses(userid,fromDateTime);
    }

    // 껓다발 확정 후, 관련 정보 DB에 저장.
    public void saveBucketData(String whom,String situation, String message,String imageUrl,Long memberId,List<Long> flowerIds){
        // 사용자가 입력한 Text
        Bouquet bouquet = new Bouquet();
        bouquet.setWhom(whom);
        bouquet.setSituation(situation);
        bouquet.setMessage(message);
        bouquet.setImageUrl(imageUrl);
        // 꽃다발 저장.
        bouquetRepository.save(bouquet);

        // Member 엔티티 조회.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("부적절한 멤버 아이디"));

        // Memberbouquet 생성 및 저장.
        Memberbouquet memberbouquet = new Memberbouquet();
        memberbouquet.setBouquet(bouquet);
        memberbouquet.setMember(member);
        memberbouquetRepository.save(memberbouquet);

        // 각 Flower ID에 대해 Flowerbouquet 생성 및 저장.
        for(Long flowerId:flowerIds){
            Flower flower = flowerRepository.findById(flowerId)
                    .orElseThrow(()-> new IllegalArgumentException("적잘하지 않은 flowerId 입니다"));
            Flowerbouquet flowerbouquet=new Flowerbouquet();
            flowerbouquet.setBouquet(bouquet);
            flowerbouquet.setFlower(flower);
            flowerbouquetRepository.save(flowerbouquet);
        }

    }

    // 사용자가 입력한 text 기반 프롬프트 생성. (메인 꽃, 꽃말 기준 추천 꽃 생성)
    public String generatePrompt(String whom, String situation, String message){
        return "";
    }

    // 사용자가 선택한 꽃 기반 프롬프트 생성 (꽃말 기준 추천 꽃 생성)
    public String generatePrompt(List<String> flowers){
        return "";
    }
}
