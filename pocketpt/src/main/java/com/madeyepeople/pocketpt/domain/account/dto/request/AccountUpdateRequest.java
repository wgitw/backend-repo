package com.madeyepeople.pocketpt.domain.account.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountUpdateRequest {
    @Size(max = 60, message = "간단 소개는 60 character를 넘을 수 없습니다.")
    String introduce;

    String profilePictureUrl;
}
