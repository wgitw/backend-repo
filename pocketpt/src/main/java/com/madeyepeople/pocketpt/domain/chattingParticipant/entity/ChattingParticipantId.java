package com.madeyepeople.pocketpt.domain.chattingParticipant.entity;

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
//    @EqualsAndHashCode.Include
//    private Long chattingParticipantId;

    @EqualsAndHashCode.Include
    private Long chattingRoom;

    @EqualsAndHashCode.Include
    private Long account;
}
