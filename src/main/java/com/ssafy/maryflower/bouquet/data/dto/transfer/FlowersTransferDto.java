package com.ssafy.maryflower.bouquet.data.dto.transfer;

import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlowersTransferDto {
    private List<String> flowers;
    private String requestId;

}
