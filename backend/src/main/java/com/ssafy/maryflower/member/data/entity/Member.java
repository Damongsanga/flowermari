package com.ssafy.maryflower.member.data.entity;

import com.ssafy.maryflower.bouquet.data.entity.ApiLog;
import com.ssafy.maryflower.bouquet.data.entity.MemberBouquet;
import com.ssafy.maryflower.global.BaseEntity;
import com.ssafy.maryflower.global.auth.data.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column(unique = true)
  private String kakaoId;
  private String nickname;
  private String password;
  private String profileImage;

  @Enumerated(EnumType.STRING)
  private UserRole role; //ADMIN, MANAGER, USER

  @Builder
  public Member(Long memberId, String kakaoId, String nickname, String password, String profileImage, UserRole role) {
    this.memberId = memberId;
    this.kakaoId = kakaoId;
    this.nickname = nickname;
    this.password = password;
    this.profileImage = profileImage;
    this.role = role;
  }



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
