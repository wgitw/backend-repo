package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.response.AccountDetailGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToAccountGetResponse {
    public AccountDetailGetResponse fromAccountEntity(Account account) {
        return AccountDetailGetResponse.builder()
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
                .introduce(account.getIntroduce())
                .build();
    }
}
