package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.response.TrainerCareerCreateAndGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Career;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ToTrainerCareerCreateAndGetResponse {
    public TrainerCareerCreateAndGetResponse of(List<Career> careerList) {
        List<CareerDto> careerDtoList = careerList.stream()
                .map(career -> CareerDto.builder()
                        .careerId(career.getCareerId())
                        .type(career.getType().getValue())
                        .title(career.getTitle())
                        .date(career.getDate())
                        .build())
                .collect(Collectors.toList());;

        return TrainerCareerCreateAndGetResponse.builder()
                .careerList(careerDtoList)
                .build();
    }
}
