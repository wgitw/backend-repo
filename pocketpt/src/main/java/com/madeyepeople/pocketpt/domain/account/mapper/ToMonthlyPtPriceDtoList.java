package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToMonthlyPtPriceDtoList {
    public List<MonthlyPtPriceDto> of(List<MonthlyPtPrice> monthlyPtPriceList) {
        return monthlyPtPriceList.stream()
                .map(monthlyPtPrice -> MonthlyPtPriceDto.builder()
                        .monthlyPtPriceId(monthlyPtPrice.getMonthlyPtPriceId())
                        .period(monthlyPtPrice.getPeriod())
                        .price(monthlyPtPrice.getPrice())
                        .build())
                .toList();
    }
}
