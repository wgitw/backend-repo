package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MonthlyPtPriceGetResponse {
    private Long trainerAccountId;
    private List<MonthlyPtPriceDto> monthlyPtPriceList;
}
