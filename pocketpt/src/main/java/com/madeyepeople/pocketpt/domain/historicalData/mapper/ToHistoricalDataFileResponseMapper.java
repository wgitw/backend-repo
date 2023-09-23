package com.madeyepeople.pocketpt.domain.historicalData.mapper;

import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataFileResponse;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalDataFile;
import org.springframework.stereotype.Component;

@Component
public class ToHistoricalDataFileResponseMapper {
    public HistoricalDataFileResponse of(HistoricalDataFile historicalDataFile) {
        return HistoricalDataFileResponse.builder()
                .historicalDataFileId(historicalDataFile.getHistoricalDataFileId())
                .fileUrl(historicalDataFile.getFileUrl())
                .scope(historicalDataFile.getScope().name())
                .createdAt(historicalDataFile.getCreatedAt())
                .updatedAt(historicalDataFile.getUpdatedAt())
                .build();
    }
}
