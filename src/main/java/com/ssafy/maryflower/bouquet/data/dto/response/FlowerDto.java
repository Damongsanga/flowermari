package com.ssafy.maryflower.bouquet.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 개별 꽃에 대한 세부 데이터
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlowerDto {

    private Long flowerId;
    private String imgUrl;
    private String name;
    private String meaning;
    private String color;

}
