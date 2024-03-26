package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.FlowerData;
import com.ssafy.maryflower.bouquet.data.dto.response.FlowerDto;
import com.ssafy.maryflower.bouquet.data.entity.Flower;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import com.ssafy.maryflower.member.data.entity.Member;
import com.ssafy.maryflower.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FlowerDataLoader implements CommandLineRunner {

    private final CacheService cacheService;
    private final BouquetService bouquetService;
    private final MemberRepository memberRepository;
    private final FlowerRepository flowerRepository;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run?");
        loadFlowerData();
        loadMemberData();
        loadBouquetData();
        cacheService.getAllFlowers();
    }
    private void loadFlowerData() {
        System.out.println("flower data load");
        for (FlowerData.FlowerInfo info : FlowerData.FLOWERS) {
            Flower flower = Flower.builder()
                    .koreanName(info.getKoreanName())
                    .englishName(info.getEnglishName())
                    .color(info.getColor())
                    .meaning(info.getMeaning())
                    .imageUrl(info.getImageUrl())
                    .build();
            flowerRepository.save(flower);
        }
    }
    private void loadMemberData(){
        System.out.println("member data load");
        Member member = new Member();
        member.setKakaoId("TestKakaoId");
        member.setProfileImage("TestPorfileImage");
        memberRepository.save(member);
    }

    private void loadBouquetData(){
        System.out.println("bouquet data load");
        List<Long> testList;

        testList = new ArrayList<>();
        testList.add(2L);
        testList.add(4L);
        testList.add(7L);
        bouquetService.saveBucketData("여자친구", "기념일","축하","testUrl",1L,testList);

        testList = new ArrayList<>();
        testList.add(2L);
        testList.add(1L);
        testList.add(6L);
        bouquetService.saveBucketData("친구", "졸업","고생했어","testUrl",1L,testList);

        testList = new ArrayList<>();
        testList.add(9L);
        testList.add(10L);
        testList.add(13L);
        bouquetService.saveBucketData("동생", "생일","축하","testUrl",1L,testList);


    }
}
