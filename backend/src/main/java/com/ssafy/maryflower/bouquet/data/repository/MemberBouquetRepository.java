package com.ssafy.maryflower.bouquet.data.repository;

import com.ssafy.maryflower.bouquet.data.entitiy.MemberBouquet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBouquetRepository extends JpaRepository<MemberBouquet, Long> {

}
