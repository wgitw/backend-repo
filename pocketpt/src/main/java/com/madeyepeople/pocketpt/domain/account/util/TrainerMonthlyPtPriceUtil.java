package com.madeyepeople.pocketpt.domain.account.util;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TrainerMonthlyPtPriceUtil {
    public boolean hasDuplicatePeriod(List<MonthlyPtPriceDto> monthlyPtPriceDtoList) {
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

    public List<MonthlyPtPriceDto> sortByPeriod(List<MonthlyPtPriceDto> monthlyPtPriceDtoList) {
//        Collections.sort(monthlyPtPriceDtoList, Comparator.comparingInt(MonthlyPtPriceDto::getPeriod));
        monthlyPtPriceDtoList.sort(Comparator.comparingInt(MonthlyPtPriceDto::getPeriod));
        log.error(monthlyPtPriceDtoList.toString());
        return monthlyPtPriceDtoList;
    }
}
