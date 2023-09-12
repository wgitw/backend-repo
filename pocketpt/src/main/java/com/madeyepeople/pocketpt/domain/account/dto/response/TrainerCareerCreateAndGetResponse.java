package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrainerCareerCreateAndGetResponse {
    private List<CareerDto> careerList;
}
