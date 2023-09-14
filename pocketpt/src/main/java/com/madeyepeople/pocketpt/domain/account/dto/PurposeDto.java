package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PurposeDto {
    private Long purposeId;
    private String title;
    private String content;
    private String targetDate;
}
