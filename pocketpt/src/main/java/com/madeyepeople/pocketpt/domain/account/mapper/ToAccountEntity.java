package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.Oauth2ProviderInfo;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToAccountEntity {
    public Account toAccountCreateEntity(Oauth2ProviderInfo oauth2ProviderInfo, String encodedPassword) {
        return Account.builder()
                .oauth2Id(oauth2ProviderInfo.getOauth2Id())
                .provider(oauth2ProviderInfo.getProvider())
                .email(oauth2ProviderInfo.getEmail())
                .nickname(oauth2ProviderInfo.getNickname())
                .password(encodedPassword)
                .oauth2AccessToken(oauth2ProviderInfo.getOauth2ProviderAccessToken())
                .build();
    }
}
