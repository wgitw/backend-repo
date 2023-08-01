package com.madeyepeople.pocketpt.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.madeyepeople.pocketpt.domain.account.dto.CommonAccountInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TrainerInfoUpdateRequest {
    private CommonAccountInfo commonAccountInfo;
    @JsonIgnore
    private MultipartFile careerCertificate;
    private String career;
}
