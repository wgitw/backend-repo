package com.madeyepeople.pocketpt.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonRegistrationRequest {

    @NotBlank(message = "이름은 빈칸일 수 없습니다.")
    @Pattern(regexp = "^[가-힣]+$|^[a-zA-Z]+$", message = "이름은 영어로만 또는 한글로만 이루어져야 합니다.")
    @Size(max = 25, message = "이름은 25 character를 넘을 수 없습니다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 11개의 숫자여야합니다.")
    private String phoneNumber;

    @Size(max = 25, message = "닉네임은 25 character를 넘을 수 없습니다.")
    private String nickname;
}
