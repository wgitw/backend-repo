package com.madeyepeople.pocketpt.global.util;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtil {

    private final AccountRepository accountRepository;

    public Long getLoginUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails accountPrincipal = (UserDetails) authentication.getPrincipal();
                username = accountPrincipal.getUsername();
                Long accountId = isAccountExist(username);
                return accountId;
            }
        } catch (Exception e) {
            // TODO: exception handling
            log.error("User not authenticated");
            log.error(e.getMessage());
        }
        return null;
    }

    public Long isAccountExist(String email) throws Exception {
        Optional<Account> account = accountRepository.findByEmailAndIsDeletedFalse(email);

        if (account.isPresent()) {
            return account.get().getAccountId();
        }

        throw new Exception("해당 이메일의 사용자가 존재하지 않습니다.");
    }
}
