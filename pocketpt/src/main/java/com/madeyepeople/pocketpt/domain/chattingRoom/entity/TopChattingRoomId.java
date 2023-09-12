package com.madeyepeople.pocketpt.domain.chattingRoom.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TopChattingRoomId {
    @EqualsAndHashCode.Include
    private Long account;

    @EqualsAndHashCode.Include
    private Long chattingRoom;
}
