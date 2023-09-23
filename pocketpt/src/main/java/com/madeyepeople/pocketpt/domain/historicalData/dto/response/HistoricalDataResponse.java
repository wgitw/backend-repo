package com.madeyepeople.pocketpt.domain.historicalData.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class HistoricalDataResponse {
    private Long historicalDataId;
    private Date date;
    private Long accountId;
    private String title;
    private String description;
    private List<HistoricalDataFileResponse> historicalDataFileResponseMapperList;
    private String scope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
