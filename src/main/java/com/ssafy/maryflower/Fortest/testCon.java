package com.ssafy.maryflower.Fortest;

import com.ssafy.maryflower.bouquet.data.entitiy.ApiLog;
import com.ssafy.maryflower.bouquet.data.repository.ApiLogRepository;
import com.ssafy.maryflower.member.data.entitiy.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class testCon {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ApiLogRepository apiLogRepository;

    @PostMapping("/test")
    @Transactional
    public ResponseEntity<String> testdbConnection(){
        ApiLog apiLog=new ApiLog();
        apiLog.setMember(new Member());

        apiLogRepository.save(apiLog);
//        entityManager.persist(apiLog);
//        entityManager.flush();

        System.out.println(apiLogRepository.findById(1L));
        return ResponseEntity.ok("ok");
    }

}
