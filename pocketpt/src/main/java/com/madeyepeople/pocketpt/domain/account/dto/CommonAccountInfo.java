package com.madeyepeople.pocketpt.domain.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonAccountInfo {
    private String name;
    private String phoneNumber;
    private String nickname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String recommenderCode;
    private String introduce;
    private String expertise;
}
