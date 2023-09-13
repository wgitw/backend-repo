package com.madeyepeople.pocketpt.domain.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TrainerMonthlyPtPriceCreateRequest {
    private Integer period;
    private Integer price;
}
