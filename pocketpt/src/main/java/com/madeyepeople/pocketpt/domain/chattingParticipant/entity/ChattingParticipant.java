package com.madeyepeople.pocketpt.domain.chattingParticipant.entity;

import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table
@IdClass(ChattingParticipantId.class)
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

    @OneToMany(mappedBy = "chattingParticipant")
    private List<ChattingMessage> chattingMessageList;

    @Column(name = "participant_account_id", nullable = false)
    private Long accountId;

    @Column(name = "is_host", nullable = false)
    @Builder.Default
    private Boolean isHost = Boolean.FALSE;

    @Column(name = "chatting_room_entry_time")
    private LocalDateTime chattingRoomEntryTime;

    @Column(name = "chatting_room_exit_time")
    private LocalDateTime chattingRoomExitTime;

    @Column(name = "simp_session_id")
    private String simpSessionId;

    @Column(name = "not_view_count", nullable = false)
    @Builder.Default
    private int notViewCount = 0;

}
