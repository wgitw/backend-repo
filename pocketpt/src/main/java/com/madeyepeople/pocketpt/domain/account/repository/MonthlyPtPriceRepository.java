package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyPtPriceRepository extends JpaRepository<MonthlyPtPrice, Long> {

    // 해당 트레이너의 모든 단가 삭제
    void deleteAllByTrainerAccountId(Long accountId);
}
