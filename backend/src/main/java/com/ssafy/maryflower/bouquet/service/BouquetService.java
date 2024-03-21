package com.ssafy.maryflower.bouquet.service;


import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
import com.ssafy.maryflower.bouquet.data.repository.ApiLogRepository;
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

    // 사용자가 입력한 text 기반 프롬프트 생성. (메인 꽃, 꽃말 기준 추천 꽃 생성)
    public String generatePrompt(String whom, String situation, String message){
        return "";
    }

    // 사용자가 선택한 꽃 기반 프롬프트 생성 (꽃말 기준 추천 꽃 생성)
    public String generatePrompt(List<String> flowers){
        return "";
    }
}
