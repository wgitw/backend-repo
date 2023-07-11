package com.madeyepeople.pocketpt.domain.chattingMessage.entity;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "chatting_message")
@EqualsAndHashCode(callSuper=false)
public class ChattingMessage extends BaseEntity {
    @Id
    @Column(name = "chatting_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_participant_id")
    private ChattingParticipant chattingParticipant;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = Boolean.FALSE;

    @Column(name = "is_bookmarked", nullable = false)
    @Builder.Default
    private Boolean isBookmarked = Boolean.FALSE;

}
