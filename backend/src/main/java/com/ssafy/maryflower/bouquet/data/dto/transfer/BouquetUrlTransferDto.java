package com.ssafy.maryflower.bouquet.data.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
AI서버로부터 꽃다발이 생성된 후 그것이 저장된 url을 받는 dto
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BouquetUrlTransferDto {
    private String requestId;
    private String bouquetUrl;
}
