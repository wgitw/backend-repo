package com.madeyepeople.pocketpt.domain.account.service;

import com.madeyepeople.pocketpt.global.constants.Role;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.mapper.ToAccountGetResponse;
import com.madeyepeople.pocketpt.domain.account.mapper.ToRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import com.madeyepeople.pocketpt.global.util.UniqueCodeGenerator;
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
    private final ToRegistrationResponse toRegistrationResponse;
    private final ToAccountGetResponse toAccountGetResponse;
    private final SecurityUtil securityUtil;
    private final UniqueCodeGenerator uniqueCodeGenerator;

    @Transactional
    public AccountRegistrationResponse registerAccount(CommonRegistrationRequest commonRegistrationRequest, String role) {
        Long accountId = securityUtil.getLoginAccountId();
        Optional<Account> account = accountRepository.findByAccountIdAndIsDeletedFalse(accountId);

        if (account.isPresent()) {
            Account changed = account.get().updateByRegistrationRequest(
                    commonRegistrationRequest.getName(),
                    commonRegistrationRequest.getPhoneNumber(),
                    commonRegistrationRequest.getNickname(),
                    Role.valueOf(role.toUpperCase()),
                    uniqueCodeGenerator.getUniqueCode()
            );
            // TODO: save 과정에서 identificationCode 중복이 발생할 수 있음.
            //  Account entity에 unique constraint 적용해뒀으니 exception handling 필요. 다시 code 생성하던가.
            Account saved = accountRepository.save(changed);
            return toRegistrationResponse.fromAccountEntity(saved);
        } else {
            String msg = CustomExceptionMessage.AUTHENTICATED_USER_NOT_FOUND.getMessage();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    public AccountDetailGetResponse getAccount() {
        Account account = securityUtil.getLoginAccountEntity();
        return toAccountGetResponse.fromAccountEntity(account);
    }
}
