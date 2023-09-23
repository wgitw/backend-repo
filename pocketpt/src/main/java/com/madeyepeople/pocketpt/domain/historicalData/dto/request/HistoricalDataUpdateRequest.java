package com.madeyepeople.pocketpt.domain.historicalData.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HistoricalDataUpdateRequest {
    private String date;
    private String title;
    private String description;
    private String scope;
}
