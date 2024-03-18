package com.ssafy.maryflower.bouquet.data.dto.response;

import com.ssafy.maryflower.bouquet.data.entitiy.Flower;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

// DB에 저장되어 있는 모든 꽃 데이터.
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllFlowers {
    private List<Flower> white;
    private List<Flower> yellow;
    private List<Flower> red;
    private List<Flower> pink;
    private List<Flower> purple;
}
