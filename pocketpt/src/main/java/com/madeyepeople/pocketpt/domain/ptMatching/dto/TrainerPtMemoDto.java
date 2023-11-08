package com.madeyepeople.pocketpt.domain.ptMatching.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TrainerPtMemoDto {
    private Long ptMatchingId;
    private String memo;
}
