package com.madeyepeople.pocketpt.domain.ptMatching.dto.response;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtRegistrationAcceptResponse {
    private PtMatchingSummary ptMatchingSummary;
    private Integer totalSales;
}
