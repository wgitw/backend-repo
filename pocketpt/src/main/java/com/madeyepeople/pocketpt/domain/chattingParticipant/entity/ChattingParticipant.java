package com.madeyepeople.pocketpt.domain.chattingParticipant.entity;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "chatting_participant")
@EqualsAndHashCode(callSuper=false)
@ToString
public class ChattingParticipant extends BaseEntity {
    @Id
    @Column(name = "chatting_participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingParticipantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @OneToMany(mappedBy = "chattingParticipant")
    private List<ChattingMessage> chattingMessageList;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "is_host", nullable = false)
    @Builder.Default
    private Boolean isHost = Boolean.FALSE;

}
