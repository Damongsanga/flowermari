package com.ssafy.maryflower.bouquet.data.repository;

import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Query("SELECT COUNT(a) FROM ApiLog a WHERE a.member.memberId = :userId AND a.createDateTime> :fromDateTime")
    Integer numberOfApiUses(@Param("userId") Long userId, @Param("fromDateTime") LocalDateTime fromDateTime);

}
