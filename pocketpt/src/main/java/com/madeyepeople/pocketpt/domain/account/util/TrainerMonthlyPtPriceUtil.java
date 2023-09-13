package com.madeyepeople.pocketpt.domain.account.util;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import com.madeyepeople.pocketpt.domain.account.entity.MonthlyPtPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@Slf4j
public class TrainerMonthlyPtPriceUtil {
    public boolean hasDuplicatePeriodByDto(List<MonthlyPtPriceDto> monthlyPtPriceDtoList) {
        Set<Integer> uniquePeriods = new HashSet<>();

        for (MonthlyPtPriceDto dto : monthlyPtPriceDtoList) {
            Integer period = dto.getPeriod();
            if (uniquePeriods.contains(period)) {
                return true;
            }
            uniquePeriods.add(period);
        }

        return false;
    }

    public boolean hasDuplicatePeriodByEntity(List<MonthlyPtPrice> monthlyPtPriceList) {
        Set<Integer> uniquePeriods = new HashSet<>();

        for (MonthlyPtPrice dto : monthlyPtPriceList) {
            Integer period = dto.getPeriod();
            if (uniquePeriods.contains(period)) {
                return true;
            }
            uniquePeriods.add(period);
        }

        return false;
    }

    public List<MonthlyPtPriceDto> sortByPeriod(List<MonthlyPtPriceDto> monthlyPtPriceDtoList) {
        monthlyPtPriceDtoList.sort(Comparator.comparingInt(MonthlyPtPriceDto::getPeriod));
        return monthlyPtPriceDtoList;
    }
}
