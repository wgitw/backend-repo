package com.madeyepeople.pocketpt.domain.historicalData.mapper;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataCreateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import com.madeyepeople.pocketpt.global.common.CommonFunction;
import org.springframework.stereotype.Component;

@Component
public class ToHistoricalDataEntityMapper {
    public HistoricalData of(Account account, HistoricalDataCreateRequest historicalDataCreateRequest) {
        return HistoricalData.builder()
                .date(CommonFunction.convertStringToDate(historicalDataCreateRequest.getDate()))
                .account(account)
                .title(historicalDataCreateRequest.getTitle())
                .description(historicalDataCreateRequest.getDescription())
                .scope(Scope.valueOf(historicalDataCreateRequest.getScope()))
                .build();
    }
}
