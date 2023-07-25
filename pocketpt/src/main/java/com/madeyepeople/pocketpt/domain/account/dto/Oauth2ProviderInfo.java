package com.madeyepeople.pocketpt.domain.account.dto;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Builder
@Getter
public class Oauth2ProviderInfo {
    private final Map<String, Object> attributes;
    private final Long oauth2Id;
    private final String provider;
    private final String email;
    private final String nickname;
    private final String imageUrl;
    private final Optional<Account> accountOptional;
}
