package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.PhysicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PhysicalInfoRepository extends JpaRepository<PhysicalInfo, Long> {
    boolean existsByAccountAccountIdAndDate(Long accountId, LocalDate date);
}
