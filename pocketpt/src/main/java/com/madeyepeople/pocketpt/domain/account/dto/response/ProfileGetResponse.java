package com.madeyepeople.pocketpt.domain.account.dto.response;

import com.madeyepeople.pocketpt.domain.account.dto.CareerDto;
import com.madeyepeople.pocketpt.domain.account.dto.PurposeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProfileGetResponse {
    private String role;
    private String name;
    private String profilePictureUrl;
    private String introduce;
    private List<PurposeDto> purposeDtoList;
    private List<CareerDto> careerDtoList;
}
