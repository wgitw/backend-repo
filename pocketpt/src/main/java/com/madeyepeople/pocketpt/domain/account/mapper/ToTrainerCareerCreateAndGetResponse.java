package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.TrainerCareerCreateAndGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToTrainerCareerCreateAndGetResponse {
    public TrainerCareerCreateAndGetResponse of(List<Career> careerList) {
        return TrainerCareerCreateAndGetResponse.builder()
                .trainerAccountId(careerList.get(0).getTrainer().getAccountId())
                .careerList(careerList.stream()
                        .map(career -> CareerDto.builder()
                                .careerId(career.getCareerId())
                                .type(career.getType().getValue())
                                .title(career.getTitle())
                                .date(career.getDate())
                                .build())
                        .toList()
                ).build();
    }
}
