package com.madeyepeople.pocketpt.domain.chattingParticipant.entity;

import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
