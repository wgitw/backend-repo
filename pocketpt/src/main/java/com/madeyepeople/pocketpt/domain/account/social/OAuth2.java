package com.madeyepeople.pocketpt.domain.account.social;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OAuth2 {
    private List<String> authorizedRedirectUris = new ArrayList<>();

    public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
        this.authorizedRedirectUris = authorizedRedirectUris;
        return this;
    }
}