package com.ssafy.maryflower.bouquet.data.entitiy;

import com.ssafy.maryflower.member.data.entitiy.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Memberbouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name= "bouquet_id")
    private Bouquet bouquet;


}
