package com.madeyepeople.pocketpt.domain.historicalData.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HistoricalDataCreateRequest {
    private String date;
    private String title;
    private String description;
    private MultipartFile[] file;
    private String scope;
}
