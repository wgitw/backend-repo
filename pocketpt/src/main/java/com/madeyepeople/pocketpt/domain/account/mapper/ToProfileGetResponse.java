package com.madeyepeople.pocketpt.domain.account.mapper;

import com.madeyepeople.pocketpt.domain.account.dto.response.ProfileGetResponse;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToProfileGetResponse {

    private final ToPurposeDto toPurposeDto;
    private final ToCareerDto toCareerDto;

    public ProfileGetResponse of(Account account) {
        return ProfileGetResponse.builder()
                .role(account.getAccountRole().getValue())
                .name(account.getName())
                .profilePictureUrl(account.getProfilePictureUrl())
                .introduce(account.getIntroduce())
                .purposeDtoList(toPurposeDto.of(account.getPurposeList()))
                .careerDtoList(toCareerDto.of(account.getCareerList()))
                .build();
    }
}
