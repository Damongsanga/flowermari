package com.ssafy.maryflower.bouquet.data.entitiy;

import com.ssafy.maryflower.global.BaseEntity;
import com.ssafy.maryflower.member.data.entitiy.Member;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Bouquet extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    // Bouquet 엔티티의 기본키(PK)를 자동으로 생성.
    private Long bouquetId;
    private String whom;
    private String situation;
    private String message;
    private String imageUrl;

    @ManyToMany(mappedBy = "bouquets")
    private List<Member> members=new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "flower_bouquet", // 연결테이블 이름 지정.
            joinColumns = @JoinColumn(name = "boouquet_id"), // Bouquet 측의 조인 엔티티 컬럼 설정
            inverseJoinColumns = @JoinColumn(name="flower_id") // Flower 엔티티 측의 조인컬럼 설정
    )

    private List<Flower> flowers=new ArrayList<>();
    /*
    매핑되는 상대테이블의 List를 가지고 있는 이유
    - 한 엔티티에서 연관된 다른 엔티티로의 접근과 반대 방향으로의 접근 모두 가능하게 하기 위해.
    Bouquet 엔티티에서 연관된 Flower 엔티티를 조회 할 수 있으며, 반대로도 조회 가능.
    또 데이터의 일관성을 유지. flower
     */
}
