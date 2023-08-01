package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.constants.Role;
import com.madeyepeople.pocketpt.domain.account.dto.Oauth2ProviderInfo;
import com.madeyepeople.pocketpt.domain.account.dto.request.CommonRegistrationRequest;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class ToAccountEntity {
    public Account fromOAuth2Info(Oauth2ProviderInfo oauth2ProviderInfo, String encodedPassword) {
        return Account.builder()
                .oauth2Id(oauth2ProviderInfo.getOauth2Id())
                .provider(oauth2ProviderInfo.getProvider())
                .email(oauth2ProviderInfo.getEmail())
                .nickname(oauth2ProviderInfo.getNickname())
                .profilePictureUrl(oauth2ProviderInfo.getImageUrl())
                .password(encodedPassword)
                .oauthAccessToken(oauth2ProviderInfo.getOauth2ProviderAccessToken())
                .build();
    }

    /**
     * [목적]
     * 2차 회원가입 시, request의 정보대로 Account Entity 객체를 수정.
     *
     * [삭제 사유]
     * 기존 entity를 update하는 용도로 사용할 것이면, Entity 객체 내에서 update 함수를 이용.
     * builder() 이용하면 새로운 객체 생성하니 낭비라 생각.
     */
//    public Account fromRegistrationRequest(CommonRegistrationRequest commonRegistrationRequest, Role accountRole) {
//        return Account.builder()
//                .name(commonRegistrationRequest.getName())
//                .phoneNumber(commonRegistrationRequest.getPhoneNumber())
//                .nickname(commonRegistrationRequest.getNickname())
//                .accountRole(accountRole)
//                .build();
//    }
}
