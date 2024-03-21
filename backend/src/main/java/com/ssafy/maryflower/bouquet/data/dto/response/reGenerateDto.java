package com.ssafy.maryflower.bouquet.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class reGenerateDto {

    private String bouquetUrl;

    private Integer apiUsageCount;

    private List<Long> usedFlower;

    private List<Long> recommendByMeaning;

    private List<Long> recommendByPopularity;
}
