package com.ssafy.maryflower.bouquet.data.dto.transfer;

import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlowersTransferDto {
    private List<Long> flowersId;
    private String requestId;

}
