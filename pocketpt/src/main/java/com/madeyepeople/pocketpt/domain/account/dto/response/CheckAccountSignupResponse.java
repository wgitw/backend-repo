package com.madeyepeople.pocketpt.domain.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckAccountSignupResponse {
    private Boolean isAccountSignedUp;
    private String defaultName;
}
