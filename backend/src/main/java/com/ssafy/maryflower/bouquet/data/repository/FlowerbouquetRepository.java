package com.ssafy.maryflower.bouquet.data.repository;

import com.ssafy.maryflower.bouquet.data.entitiy.Flowerbouquet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerbouquetRepository extends JpaRepository<Flowerbouquet, Long> {
}
