package com.ssafy.maryflower.bouquet.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자가 입력한 데이터를 Redis 캐시에 저장할 떄 사용.
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserDataHolder {
    private String whom;
    private String situation;
    private String message;
    private Long userId;
}
