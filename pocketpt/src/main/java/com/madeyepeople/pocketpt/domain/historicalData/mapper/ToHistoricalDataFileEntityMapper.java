package com.madeyepeople.pocketpt.domain.historicalData.mapper;

import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataCreateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataFileCreateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.HistoricalDataFileUpdateRequest;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalDataFile;
import org.springframework.stereotype.Component;

@Component
public class ToHistoricalDataFileEntityMapper {
    public HistoricalDataFile of(HistoricalData historicalData, HistoricalDataCreateRequest historicalDataCreateRequest, String fileUrl) {
        return HistoricalDataFile.builder()
                .historicalData(historicalData)
                .fileUrl(fileUrl)
                .scope(Scope.valueOf(historicalDataCreateRequest.getScope()))
                .build();
    }

    public HistoricalDataFile of(HistoricalData historicalData, HistoricalDataFileCreateRequest historicalDataFileCreateRequest, String fileUrl) {
        return HistoricalDataFile.builder()
                .historicalData(historicalData)
                .fileUrl(fileUrl)
                .scope(Scope.valueOf(historicalDataFileCreateRequest.getScope()))
                .build();
    }

    public HistoricalDataFile of(HistoricalDataFile historicalDataFile, HistoricalDataFileUpdateRequest historicalDataFileUpdateRequest) {
        historicalDataFile.setScope(Scope.valueOf(historicalDataFileUpdateRequest.getScope()));
        return historicalDataFile;
    }
}
