package com.madeyepeople.pocketpt.domain.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurposeUpdateRequest {
    private String title;
    private String content;
    private String targetDate;
}
