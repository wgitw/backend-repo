package com.madeyepeople.pocketpt.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonRegistrationRequest {
    @NotBlank(message = "Name should not be blank")
    @Pattern(regexp = "^[가-힣]+$|^[a-zA-Z]+$", message = "Name should contain only Korean or English characters")
    @Size(max = 50, message = "Name length should not exceed 50 characters")
    private String name;
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "PhoneNumber should be a string of 11 digits")
    private String phoneNumber;
}
