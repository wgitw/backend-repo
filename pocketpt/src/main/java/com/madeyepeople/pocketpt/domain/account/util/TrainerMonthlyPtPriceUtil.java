package com.madeyepeople.pocketpt.domain.account.util;

import com.madeyepeople.pocketpt.domain.account.dto.MonthlyPtPriceDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TrainerMonthlyPtPriceUtil {
    public boolean hasDuplicatePeriod(List<MonthlyPtPriceDto> monthlyPtPriceList) {
        Set<Integer> uniquePeriods = new HashSet<>();

        for (MonthlyPtPriceDto dto : monthlyPtPriceList) {
            Integer period = dto.getPeriod();
            if (uniquePeriods.contains(period)) {
                return true;
            }
            uniquePeriods.add(period);
        }

        return false;
    }
}
