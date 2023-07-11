package com.madeyepeople.pocketpt.domain.chattingMessage.dto.request;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ChattingMessageCreateRequest {
    private final ChattingParticipant chattingParticipant;

    private final String content;

    private final MultipartFile file;
}
