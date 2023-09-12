package com.madeyepeople.pocketpt.domain.chattingRoom.entity;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmarkId;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@ToString
@IdClass(TopChattingRoomId.class)
@Entity(name = "top_chatting_room")
@Access(value = AccessType.FIELD)
//@EqualsAndHashCode
public class TopChattingRoom extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;
}
