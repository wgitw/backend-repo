package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.ptMatching.dto.PtMatchingSummary;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrainerTotalSalesGetResponse {
    private Integer totalSales;
    List<PtMatchingSummary> ptMatchingSummaryList;
}
