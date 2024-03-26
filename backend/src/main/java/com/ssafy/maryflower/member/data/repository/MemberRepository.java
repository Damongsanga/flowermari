package com.ssafy.maryflower.member.data.repository;

import com.ssafy.maryflower.member.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByKakaoId(String kakaoId);
  boolean existsByKakaoId(String kakaoId);
}