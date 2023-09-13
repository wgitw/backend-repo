package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToMonthlyPtPriceDtoList {
    private final ToMonthlyPtPriceDto toMonthlyPtPriceDto;
    public List<MonthlyPtPriceDto> of(List<MonthlyPtPrice> monthlyPtPriceList) {
        return monthlyPtPriceList.stream()
                .map(toMonthlyPtPriceDto::of)
                .collect(Collectors.toList());
    }
}
