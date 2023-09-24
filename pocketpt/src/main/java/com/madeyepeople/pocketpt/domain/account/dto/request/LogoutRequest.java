package com.madeyepeople.pocketpt.domain.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LogoutRequest {
    private String accessToken;
//    private String refreshToken;
}
