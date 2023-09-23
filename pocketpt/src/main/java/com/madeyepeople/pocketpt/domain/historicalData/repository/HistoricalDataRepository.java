package com.madeyepeople.pocketpt.domain.historicalData.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {
    Optional<HistoricalData> findByHistoricalDataIdAndAccountAndIsDeletedFalse(Long historicalDataId, Account account);

    List<HistoricalData> findByAccountAndIsDeletedFalseAndDateBetween(Account account, Date startDate, Date endDate);

    List<HistoricalData> findByAccountAndScopeAndIsDeletedFalseAndDateBetween(Account account, Scope scope, Date startDate, Date endDate);
}
