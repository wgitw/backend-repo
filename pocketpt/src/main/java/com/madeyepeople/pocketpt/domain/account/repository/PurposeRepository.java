package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {
}
