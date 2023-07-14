package com.madeyepeople.pocketpt.domain.chattingParticipant.entity;

import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Embeddable
//@Getter
//@Setter
//public class ChattingParticipantId implements Serializable {
//
//    @Id
//    @Column(name = "chatting_participant_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long chattingParticipantId;
//
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chatting_room_id")
//    private ChattingRoom chattingRoom;
//}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChattingParticipantId implements Serializable {
    @EqualsAndHashCode.Include
    private Long chattingParticipantId;

    @EqualsAndHashCode.Include
    private ChattingRoom chattingRoom;
}