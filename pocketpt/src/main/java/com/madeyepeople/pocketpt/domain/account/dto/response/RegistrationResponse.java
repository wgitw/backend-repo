package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.constants.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {
    private Long accountId;
    private Role role;
    private String email;
    private String name;
    private String phoneNumber;
    private String nickname;
}
