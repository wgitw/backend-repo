package com.madeyepeople.pocketpt.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurposeCreateRequest {

    @NotBlank(message = "목표는 빈칸일 수 없습니다.")
    private String title;

    private String content;

    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
    private String targetDate;
}
