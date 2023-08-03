package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.global.constants.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class AccountDetailGetResponse {
    private Long accountId;
    private Role role;
    private String provider;
    private String email;
    private String name;
    private String phoneNumber;
    private String nickname;
    private String profilePictureUrl;
    private String gender;
    private Date birthdate;
    private String introduce;
}
