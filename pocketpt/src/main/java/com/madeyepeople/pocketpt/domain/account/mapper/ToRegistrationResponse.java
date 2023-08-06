package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.response.AccountRegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToRegistrationResponse {
    public AccountRegistrationResponse fromAccountEntity(Account account) {
        return AccountRegistrationResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .build();
    }
}
