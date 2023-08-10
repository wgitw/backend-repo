package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class MonthlyPtPriceDto {

    private Integer period;
    private Integer price;

    @Builder
    public MonthlyPtPriceDto(Integer period, Integer price) {
        this.period = period;
        this.price = price;
    }
}
