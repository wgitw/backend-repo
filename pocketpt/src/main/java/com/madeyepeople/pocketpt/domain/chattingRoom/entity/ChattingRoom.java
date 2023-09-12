package com.madeyepeople.pocketpt.domain.chattingRoom.entity;

import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@ToString
@Entity(name = "chatting_room")
//@EqualsAndHashCode
public class ChattingRoom extends BaseEntity {
    public enum ChattingRoomStatus{
        ACTIVATE, DEACTIVATE;
    }

    @Id
    @Column(name = "chatting_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomId;

    @OneToMany(mappedBy = "chattingRoom")
    private List<ChattingParticipant> chattingParticipantList;

    @OneToMany(mappedBy = "chattingRoom")
    private List<ChattingMessageBookmark> chattingMessageBookmarkList;

    @OneToMany(mappedBy = "chattingRoom")
    private List<TopChattingRoom> topChattingRoomList;

    // 두 명 대상인 경우: 채팅 상대방의 이름으로 설정 -> 상대방 이름이 변경될 경우 동기화 필요
    // 세 명 이상일 경우: 채팅방 이름 수정 가능
    @Column(name = "room_name")
    private String roomName;

    @Column(name = "status", nullable = false)
    private ChattingRoomStatus status;

    @Column(name = "host_id", nullable = false)
    private Long hostId;

    @Column(name = "number_of_participant", nullable = false)
    private int numberOfParticipant;
}
