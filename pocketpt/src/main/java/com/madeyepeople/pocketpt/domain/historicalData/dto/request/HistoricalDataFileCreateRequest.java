package com.madeyepeople.pocketpt.domain.historicalData.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HistoricalDataFileCreateRequest {
    private MultipartFile file;
    private String scope;
}
