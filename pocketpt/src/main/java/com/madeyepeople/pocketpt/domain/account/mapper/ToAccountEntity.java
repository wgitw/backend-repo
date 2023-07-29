package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.Oauth2ProviderInfo;
import com.madeyepeople.pocketpt.domain.account.dto.request.TrainerRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToAccountEntity {
    public Account toAccountEntityFromOAuth2Info(Oauth2ProviderInfo oauth2ProviderInfo, String encodedPassword) {
        return Account.builder()
                .oauth2Id(oauth2ProviderInfo.getOauth2Id())
                .provider(oauth2ProviderInfo.getProvider())
                .email(oauth2ProviderInfo.getEmail())
                .nickname(oauth2ProviderInfo.getNickname())
                .profilePictureUrl(oauth2ProviderInfo.getImageUrl())
                .password(encodedPassword)
                .oauth2AccessToken(oauth2ProviderInfo.getOauth2ProviderAccessToken())
                .build();
    }

    public Account toAccountEntityFromTrainerRegistrationRequest(TrainerRegistrationRequest trainerRegistrationRequest) {
        return Account.builder()
                .build();
    }
}
