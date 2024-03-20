package com.ssafy.maryflower.member.data.entity;
//
//import com.ssafy.maryflower.bouquet.data.entity.ApiLog;
//import com.ssafy.maryflower.bouquet.data.entity.Bouquet;
import com.ssafy.maryflower.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  private String kakaoId;
  private String nickname;
  private String profileImage;

  // ApiLog에 있는 member을 기준으로 매핑.
//  @OneToMany(mappedBy="member")
//  // Member 테이블이 여러개의 ApiLog테이블과 매핑될 수 있으므로 List로 관리.
//  private List<ApiLog> apiLogs=new ArrayList<>();
//
//  /*
//   다대다 관게
//   다대다 관계는 중간 테이블이 필요
//   중간 테이블에 대한 정보 명시
//
//   */
//  @ManyToMany
//  // JoinTable은 주로 다대다관계에서 연관관계의 주인 쪽에서 정의하는게 일반적.
//  @JoinTable(
//      name = "member_bouquet", // 중간 테이블 이름.
//      joinColumns = @JoinColumn(name="member_id"), // member 엔터티 측의 조인 컬럼 설정.
//      inverseJoinColumns = @JoinColumn(name = "bouquet_id") // bouquet 엔터티 측의 조인 컬럼 설정.
//  )
//  // 매핑되는 상대 엔티티 List 저장. .
//  private List<Bouquet> bouquets=new ArrayList<>();
}


