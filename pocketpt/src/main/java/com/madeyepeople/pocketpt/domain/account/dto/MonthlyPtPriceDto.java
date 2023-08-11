package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class MonthlyPtPriceDto {

    private Long monthlyPtPriceId;
    private Integer period;
    private Integer price;

    @Builder
    public MonthlyPtPriceDto(Long monthlyPtPriceId, Integer period, Integer price) {
        this.monthlyPtPriceId = monthlyPtPriceId;
        this.period = period;
        this.price = price;
    }
}
