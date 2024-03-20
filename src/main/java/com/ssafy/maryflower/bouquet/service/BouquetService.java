package com.ssafy.maryflower.bouquet.service;


import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.FlowersTransferDto;
import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
import com.ssafy.maryflower.bouquet.data.repository.ApiLogRepository;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.infrastructure.RedisEventPublisher;
import com.ssafy.maryflower.member.data.entitiy.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public String generatePrompt(String whom, String situation, String message){
        return "";
    }
}
