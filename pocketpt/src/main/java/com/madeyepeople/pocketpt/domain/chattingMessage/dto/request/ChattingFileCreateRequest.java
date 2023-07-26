package com.madeyepeople.pocketpt.domain.chattingMessage.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChattingFileCreateRequest {
    private MultipartFile file;
}
