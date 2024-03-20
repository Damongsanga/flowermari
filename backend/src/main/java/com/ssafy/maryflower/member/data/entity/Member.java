package com.ssafy.maryflower.member.data.entity;

//import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
//import com.ssafy.maryflower.bouquet.data.entitiy.Bouquet;
//import com.ssafy.maryflower.bouquet.data.entitiy.Memberbouquet;
//import com.ssafy.maryflower.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  private String kakaoId;
  private String nickname;
  private String profileImage;

  private String name;
  private String role;

  @Builder
  public Member(String role, String name, String kakaoId, String nickname) {
    this.role = role;
    this.name = name;
    this.kakaoId = kakaoId;
    this.nickname = nickname;
  }

//  // ApiLog에 있는 member을 기준으로 매핑.
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
//  @OneToMany(mappedBy = "member")
//  private List<Memberbouquet> memberbouquets=new ArrayList<>();
//  // 매핑되는 상대 엔티티 List 저장. .
}

