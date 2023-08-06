package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChattingMessageBookmarkId {
//    @EqualsAndHashCode.Include
//    private Long chattingMessageBookmarkId;

    @EqualsAndHashCode.Include
    private Long chattingRoom;

    @EqualsAndHashCode.Include
    private Long account;

    @EqualsAndHashCode.Include
    private Long chattingMessage;
}
