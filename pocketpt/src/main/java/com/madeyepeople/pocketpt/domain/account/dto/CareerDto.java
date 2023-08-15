package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CareerDto {
    private Long careerId;
    private String type;
    private String title;
    private String date;
}
