package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

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
    private List<MonthlyPtPriceDto> monthlyPtPriceDtoList;
}
