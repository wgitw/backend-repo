package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.TrainerCareerCreateAndGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ToTrainerCareerCreateAndGetResponse {

    private final ToCareerDto toCareerDto;

    public TrainerCareerCreateAndGetResponse of(List<Career> careerList) {
        List<CareerDto> careerDtoList = toCareerDto.of(careerList);

        return TrainerCareerCreateAndGetResponse.builder()
                .careerList(careerDtoList)
                .build();
    }
}
