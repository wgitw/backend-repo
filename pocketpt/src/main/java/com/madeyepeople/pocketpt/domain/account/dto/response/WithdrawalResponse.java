package com.madeyepeople.pocketpt.domain.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WithdrawalResponse {
    private Long accountId;
    private String name;
}
