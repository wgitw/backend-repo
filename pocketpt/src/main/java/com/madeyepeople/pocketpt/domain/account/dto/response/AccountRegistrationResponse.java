package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccountRegistrationResponse {
    private Long accountId;
    private Role role;
    private String identificationCode;
    private String email;
    private String name;
    private String phoneNumber;
    private String nickname;
    private List<MonthlyPtPriceDto> monthlyPtPriceList;
}
