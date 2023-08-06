package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table
@IdClass(ChattingMessageBookmarkId.class)
@Entity(name = "chatting_message_bookmark")
@Access(value = AccessType.FIELD)
public class ChattingMessageBookmark extends BaseEntity {
//    @SequenceGenerator(
//        name = "chatting_message_bookmark_id_seq",
//        sequenceName = "chatting_message_bookmark_id_seq",
//        allocationSize = 1
//    )
//    @Id
//    @Column(name = "chatting_message_bookmark_id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatting_message_bookmark_id_seq")
//    private Long chattingMessageBookmarkId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_message_id", nullable = false)
    private ChattingMessage chattingMessage;
}
