package com.madeyepeople.pocketpt.domain.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountUpdateResponse {
    private String introduce;
    private String profileImageUrl;
}
