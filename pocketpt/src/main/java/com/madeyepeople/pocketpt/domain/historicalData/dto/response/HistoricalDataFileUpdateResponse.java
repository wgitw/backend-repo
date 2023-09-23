package com.madeyepeople.pocketpt.domain.historicalData.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class HistoricalDataFileUpdateResponse {
    private Long historicalDataFileId;
    private String fileUrl;
    private String scope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
