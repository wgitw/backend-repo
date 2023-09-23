package com.madeyepeople.pocketpt.domain.historicalData.mapper;

import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataFileResponse;
import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataResponse;
import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataUpdateResponse;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToHistoricalDataResponseMapper {
    public HistoricalDataResponse of(HistoricalData historicalData, List<HistoricalDataFileResponse> historicalDataFileResponseMapperList) {
        return HistoricalDataResponse.builder()
                .historicalDataId(historicalData.getHistoricalDataId())
                .date(historicalData.getDate())
                .accountId(historicalData.getAccount().getAccountId())
                .title(historicalData.getTitle())
                .description(historicalData.getDescription())
                .historicalDataFileResponseMapperList(historicalDataFileResponseMapperList)
                .scope(historicalData.getScope().name())
                .createdAt(historicalData.getCreatedAt())
                .updatedAt(historicalData.getUpdatedAt())
                .build();
    }

    public HistoricalDataUpdateResponse of(HistoricalData historicalData) {
        return HistoricalDataUpdateResponse.builder()
                .historicalDataId(historicalData.getHistoricalDataId())
                .date(historicalData.getDate())
                .accountId(historicalData.getAccount().getAccountId())
                .title(historicalData.getTitle())
                .description(historicalData.getDescription())
                .scope(historicalData.getScope().name())
                .createdAt(historicalData.getCreatedAt())
                .updatedAt(historicalData.getUpdatedAt())
                .build();
    }
}
