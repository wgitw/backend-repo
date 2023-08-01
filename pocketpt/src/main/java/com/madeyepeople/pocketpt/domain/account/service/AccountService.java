package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.domain.account.constants.Role;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.RegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.mapper.ToAccountEntity;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final ToAccountEntity toAccountEntity;
    private final SecurityUtil securityUtil;

    @Transactional
    public RegistrationResponse registerAccount(CommonRegistrationRequest commonRegistrationRequest, String role) {
        Long accountId = securityUtil.getLoginAccountId();
        Optional<Account> account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId);

//        if (account.isPresent()) {
//            Account newAccount = toAccountEntity.fromRegistrationRequest(commonRegistrationRequest, Role.valueOf(role));
//            accountRepository.save(newAccount);
//            return RegistrationResponse.builder()
//                    .accountId(newAccount.getAccountId())
//                    .build();
//        }

        return null;
    }
}
