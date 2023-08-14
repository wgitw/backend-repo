package com.madeyepeople.pocketpt.domain.account.dto.request;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TrainerCareerCreateRequest {
    private List<CareerDto> careerList;
}
