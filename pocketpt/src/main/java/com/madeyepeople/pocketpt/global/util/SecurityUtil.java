package com.madeyepeople.pocketpt.global.util;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.error.exception.authorizationException.AccountNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtil {

    private final AccountRepository accountRepository;

    public Long getLoginAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails accountPrincipal = (UserDetails) authentication.getPrincipal();
                username = accountPrincipal.getUsername();
                Account account = isAccountExist(username);
                return account.getAccountId();
            }
        } catch (Exception e) {
            // TODO: exception handling
            throw new RuntimeException("User not authenticated");
        }
        return null;
    }

    public Account getLoginAccountEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails accountPrincipal = (UserDetails) authentication.getPrincipal();
                username = accountPrincipal.getUsername();
                Account account = isAccountExist(username);
                return account;
            }
        } catch (AccountNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("User not authenticated");
        }
        return null;
    }

    public Account getLoginTrainerEntity() {
        Account trainer = getLoginAccountEntity();
        if (!trainer.getAccountRole().equals(Role.TRAINER)) {
            throw new BusinessException(ErrorCode.TRAINER_CAREER_ERROR, CustomExceptionMessage.AUTHENTICATED_USER_IS_NOT_TRAINER.getMessage());
        }
        return trainer;
    }

    public Account getLoginAccountEntity(String username) {
        try {
            Account account = isAccountExist(username);
            return  account;
        } catch (Exception e) {
            // TODO: exception handling
            e.printStackTrace();
            throw new RuntimeException("User not authenticated");
        }
    }

    public Account isAccountExist(String email) throws Exception {
        Optional<Account> account = accountRepository.findByEmailAndIsDeletedFalse(email);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountNotExistException(CustomExceptionMessage.VALID_TOKEN_EMAIL_NOT_FOUND.getMessage());
        }
    }
}
