package com.madeyepeople.pocketpt.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.dto.CommonRegistrationInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TrainerRegistrationRequest {
    private CommonRegistrationInfo commonRegistrationInfo;
    @JsonIgnore
    private MultipartFile careerCertificate;
    private String career;
}
