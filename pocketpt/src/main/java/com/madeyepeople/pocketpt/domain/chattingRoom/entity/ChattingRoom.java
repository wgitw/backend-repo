package com.madeyepeople.pocketpt.domain.chattingRoom.entity;

import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "chatting_room")
@EqualsAndHashCode(callSuper=false)
public class ChattingRoom extends BaseEntity {
    public enum ChattingRoomStatus{
        ACTIVATE, DEACTIVATE;
    }

    @OneToMany(mappedBy = "chattingRoom")
    private List<ChattingParticipant> chattingParticipantList;

    @Id
    @Column(name = "chatting_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomId;

    // 두 명 대상인 경우: 채팅 상대방의 이름으로 설정 -> 상대방 이름이 변경될 경우 동기화 필요
    // 세 명 이상일 경우: 채팅방 이름 수정 가능
    @Column(name = "roomName", nullable = false)
    private String roomName;

    @Column(name = "status", nullable = false)
    private ChattingRoomStatus status;

    @Column(name = "hostId", nullable = false)
    private Long hostId;

}
