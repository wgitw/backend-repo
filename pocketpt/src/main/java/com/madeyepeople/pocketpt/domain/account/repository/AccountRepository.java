package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailAndIsDeletedFalse(String email);
    Optional<Account> findByAccountIdAndIsDeletedFalse(Long accountId);
}
