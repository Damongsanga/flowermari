package com.ssafy.maryflower.bouquet.data.entitiy;

import com.ssafy.maryflower.global.BaseEntity;
import com.ssafy.maryflower.member.data.entitiy.Member;
import jakarta.persistence.*;
import lombok.Setter;


@Setter
@Entity
public class ApiLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    /*
     연관관계의 주인.
     다 대 일 관계
     member_id 칼럼을 통해 조인.
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
