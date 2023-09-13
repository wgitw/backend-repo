package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.stereotype.Component;

@Component
public class ToMonthlyPtPriceDto {
    public MonthlyPtPriceDto of(MonthlyPtPrice monthlyPtPrice) {
        return MonthlyPtPriceDto.builder()
                .monthlyPtPriceId(monthlyPtPrice.getMonthlyPtPriceId())
                .period(monthlyPtPrice.getPeriod())
                .price(monthlyPtPrice.getPrice())
                .build();
    }
}
