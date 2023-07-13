package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {

}
