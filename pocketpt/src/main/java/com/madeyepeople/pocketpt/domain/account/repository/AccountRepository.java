package com.madeyepeople.pocketpt.domain.account.repository;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailAndIsDeletedFalse(String email);
    Optional<Account> findByAccountIdAndIsDeletedFalse(Long accountId);
    Optional<Account> findByIdentificationCodeAndIsDeletedFalse(String identificationCode);

    @Modifying
    @Query(value =
            """
                UPDATE account
                SET 
                    email = '', 
                    name = '탈퇴한 회원', 
                    phone_number = '', 
                    profile_picture_url = '', 
                    identification_code = null, 
                    is_deleted = true 
                WHERE account_id = ?
            """, nativeQuery = true)
    int deleteByAccountId(Long accountId);
}
