package com.ssafy.maryflower.member.data.entitiy;

import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
import com.ssafy.maryflower.bouquet.data.entitiy.Bouquet;
import com.ssafy.maryflower.bouquet.data.entitiy.MemberBouquet;
import com.ssafy.maryflower.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Entity
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String kakaoId;
    private String nickname;
    private String profileImage;

    // ApiLog에 있는 member을 기준으로 매핑.
    @OneToMany(mappedBy = "member")
    // Member 테이블이 여러개의 ApiLog테이블과 매핑될 수 있으므로 List로 관리.
    private List<ApiLog> apiLogs=new ArrayList<>();

    /*
     다대다 관게
     다대다 관계는 중간 테이블이 필요
     중간 테이블에 대한 정보 명시

     */
    @OneToMany(mappedBy = "member")
    private List<MemberBouquet> MemberBouquets=new ArrayList<>();
    // 매핑되는 상대 엔티티 List 저장. .
}
