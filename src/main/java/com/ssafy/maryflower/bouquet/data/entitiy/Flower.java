package com.ssafy.maryflower.bouquet.data.entitiy;

import com.ssafy.maryflower.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
public class Flower extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flowerId;

    private String englishName;
    private String koreanName;
    private String color;
    private String meaning;
    private String imageUrl;

    // Bouquet 엔티티와의 다대다 관계의 반대편을 나타냄
    // "flowers"는 Bouquet 엔티티 내에서 이 관계를 매핑하는 테이블 이름.

    /*
     연관 관계의 주인이 아닌 쪽을 지정.
     연관 관계의 주인은 외래 키를 직접 관리하며, 주인이 아닌 쪽은 단지 연관관계를 반영하기 위해 존재.
     */

    @OneToMany(mappedBy = "flower")
    private List<Flowerbouquet> flowerBouquets = new ArrayList<>();



}
