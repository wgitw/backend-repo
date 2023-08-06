package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountRegistrationResponse {
    private Long accountId;
    private Role role;
    private String email;
    private String name;
    private String phoneNumber;
    private String nickname;
}
