package com.madeyepeople.pocketpt.domain.account.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AccountProfilePictureUpdateRequest {
    private MultipartFile file;
}
