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
@Getter
@Setter
@Table
@IdClass(ChattingParticipantId.class)
//@ToString
@Entity(name = "chatting_participant")
@Access(value = AccessType.FIELD)
public class ChattingParticipant extends BaseEntity {
    @SequenceGenerator(
            name = "chatting_participant_id_seq",
            sequenceName = "chatting_participant_id_seq",
            allocationSize = 1
    )
    @Id
    @Column(name = "chatting_participant_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatting_participant_id_seq")
    private Long chattingParticipantId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;
//    @EmbeddedId
//    private ChattingParticipantId chattingParticipantId;

    @OneToMany(mappedBy = "chattingParticipant")
    private List<ChattingMessage> chattingMessageList;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "is_host", nullable = false)
    @Builder.Default
    private Boolean isHost = Boolean.FALSE;

}
