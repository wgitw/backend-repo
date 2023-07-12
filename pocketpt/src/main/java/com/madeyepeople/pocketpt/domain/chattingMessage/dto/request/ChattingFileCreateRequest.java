package com.madeyepeople.pocketpt.domain.chattingMessage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ChattingFileCreateRequest {
    private final Long chattingRoomId;

    private final Long chattingParticipantId;

    private final MultipartFile file;
}
