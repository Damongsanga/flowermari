package com.ssafy.maryflower.member.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberDto {
  private Long memberId;
  private String email;
  private String platform;
  private String refreshToken;
}
