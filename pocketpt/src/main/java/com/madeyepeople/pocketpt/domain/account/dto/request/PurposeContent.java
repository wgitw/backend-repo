package com.madeyepeople.pocketpt.domain.account.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurposeContent {
    @NotNull
    private String title;
    private String content;
    private String targetDate;
}
