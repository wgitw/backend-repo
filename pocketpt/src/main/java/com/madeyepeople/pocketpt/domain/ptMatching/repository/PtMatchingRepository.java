package com.madeyepeople.pocketpt.domain.ptMatching.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtMatchingRepository extends JpaRepository<Account, Long> {

}
