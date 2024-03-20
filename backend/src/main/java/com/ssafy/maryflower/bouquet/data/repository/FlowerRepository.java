package com.ssafy.maryflower.bouquet.data.repository;

import com.ssafy.maryflower.bouquet.data.dto.response.FlowerDto;
import com.ssafy.maryflower.bouquet.data.entitiy.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface FlowerRepository extends JpaRepository<Flower, Long> {

    // 이름으로 id 조회
    @Query("SELECT f.flowerId FROM Flower f WHERE f.koreanName= :name")
    Optional<Long> findFlowerByName(@Param("name") String name);

    @Query(value =
            "SELECT f.flower_id,  COUNT(fb.bouquet_id) AS usageCount " +
                    "FROM flower f " +
                    "JOIN flower_bouquet fb ON f.flower_id = fb.flower_id " +
                    "GROUP BY f.flower_id " +
                    "ORDER BY usageCount DESC LIMIT 7",
            nativeQuery = true)
    List<Long> findTopUsedFlowers();

}
/*
JpaRepository<Flower, Long> 에서 Flower의 자리의 매개 변수는 리포지토리가 관리할 엔티티 타입을 지정.
즉, 이 레포지토리를 통해 데이터베이스에서 'Flwer' 타입의 엔티티 객체를 저장, 조회, 삭제 등의 작업을 수행 할 수 있음
Long : 해당 엔터티의 기본키의 데이터 타입
 */

