package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.response.AccountGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToAccountGetResponse {
    public AccountGetResponse fromAccountEntity(Account account) {
        return AccountGetResponse.builder()
                .accountId(account.getAccountId())
                .role(account.getAccountRole())
                .provider(account.getProvider())
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .nickname(account.getNickname())
                .profilePictureUrl(account.getProfilePictureUrl())
                .gender(account.getGender())
                .birthdate(account.getBirthdate())
                .build();
    }
}
