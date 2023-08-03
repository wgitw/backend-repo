package com.madeyepeople.pocketpt.domain.ptMatching.dto.response;

import com.madeyepeople.pocketpt.global.constants.PtStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtRegistrationResponse {
    private Long ptMatchingId;
    private Long trainerId;
    private Long traineeId;
    private PtStatus status;
}
