package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private LocalDateTime birthdate;
    private String introduce;
    private String identificationCode;
    private Integer totalSales;
    private List<PurposeDto> purposeList;
    private List<CareerDto> careerList;
    private List<MonthlyPtPriceDto> monthlyPtPriceList;
}
