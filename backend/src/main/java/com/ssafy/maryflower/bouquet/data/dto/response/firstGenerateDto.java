package com.ssafy.maryflower.bouquet.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
 처음 요청시 클라이언트에게 반환할 dto
 생성된 꽃다발 url
 사용자가 api를 사용한 횟수
 꽃다발에 사용된 꽃들 세부 데이터
 꽃다발에 사용된 꽃들의 뺵업 꽃 세부 데이터
 DB에 저장된 모든 꽃 세부 데이터.
 */
@Getter
@AllArgsConstructor
@Setter
public class firstGenerateDto {

    public firstGenerateDto(){
        this.usedFlower=new ArrayList<>();
        this.recommendByMeaning=new ArrayList<>();
        this.recommendByPopularity=new ArrayList<>();
        this.allFlowers=new ArrayList<>();
    }

    // 꽃다발이 저장된 url
    private String bouquetUrl;

    // 사용자의 꽃다발 생성 요청 횟수.
    private Integer apiUsageCount;

    // 꽃다발 생성에 사용된 꽃 데이터
    private List<Long> usedFlower;

    private List<Long> recommendByMeaning;

    private List<Long> recommendByPopularity;

    private List<FlowerDto> allFlowers;

}
