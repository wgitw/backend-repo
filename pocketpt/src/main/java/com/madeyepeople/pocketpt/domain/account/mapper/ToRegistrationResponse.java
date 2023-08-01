package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.response.RegistrationResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToRegistrationResponse {
    public RegistrationResponse fromAccountEntity(Account account) {
        return RegistrationResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .build();
    }
}
