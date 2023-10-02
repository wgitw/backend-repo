//package com.madeyepeople.pocketpt.domain.chattingMessage.repository;
//
//import com.madeyepeople.pocketpt.domain.account.constant.Role;
//import com.madeyepeople.pocketpt.domain.account.entity.Account;
//import com.madeyepeople.pocketpt.domain.chattingMessage.entity.ChattingMessage;
//import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
//import com.madeyepeople.pocketpt.domain.chattingRoom.entity.ChattingRoom;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ChattingMessageRepositoryTest {
//    ChattingMessageRepository chattingMessageRepository;
//
//    @DisplayName("채팅방의 가장 최근 메시지를 불러올 수 있다.")
//    @Test
//    void findLatestChattingMessageByRoom() {
//        // given -> 어떤 데이터가 있을 때
//        /*
//        * 채팅방 있어야댐
//        * 메시지가 여러개 있어야댐
//        * -> Repository save
//        * */
//        final Account trainer = new Account(
//                1L,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                Role.TRAINER,
//                null,
//                "TRAINER@test.com",
//                "TRAINER",
//                "01012345678",
//                "TRAINER",
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        final Account trainee = new Account(
//                2L,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                Role.TRAINEE,
//                null,
//                "TRAINEE@test.com",
//                "TRAINEE",
//                "01012345678",
//                "TRAINEE",
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        final ChattingRoom chattingRoom = new ChattingRoom(
//                1L,
//                null,
//                null,
//                null,
//                "test",
//                ChattingRoom.ChattingRoomStatus.ACTIVATE,
//                1L,
//                2
//        );
//
//        final ChattingParticipant chattingParticipant1 = new ChattingParticipant(
//                chattingRoom,
//                trainer,
//                null,
//                Boolean.TRUE,
//                null,
//                null,
//                null,
//                0
//        );
//
//        final ChattingParticipant chattingParticipant2 = new ChattingParticipant(
//                chattingRoom,
//                trainee,
//                null,
//                Boolean.TRUE,
//                null,
//                null,
//                null,
//                0
//        );
//
//        final ChattingMessage chattingMessage = new ChattingMessage(
//                1L,
//                chattingParticipant1,
//                null,
//                "test1",
//                null,
//                Boolean.FALSE,
//                0
//        );
//
//        final ChattingMessage chattingMessage2 = new ChattingMessage(
//                2L,
//                chattingParticipant1,
//                null,
//                "test2",
//                null,
//                Boolean.FALSE,
//                0
//        );
//
//        final ChattingMessage chattingMessage3 = new ChattingMessage(
//                3L,
//                chattingParticipant1,
//                null,
//                "tes3",
//                null,
//                Boolean.FALSE,
//                0
//        );
//
//        // when -> 어떤 행동을 했을 때
//        /*
//        * 검증할 메서드를 호출하고 결과값을 받아온다
//        * */
//        chattingMessageRepository.findLatestChattingMessageByRoom(1L);
//
//        // then -> 어떤 결과가 나온다
//        /*
//        * 결과값이 원하는대로 나오는지 검증한다
//        * */
//
//    }
//
//    @DisplayName("채팅방의 메시지가 없을 경우 아무 데이터도 불러오지 않는다.")
//    @Test
//    void name() {
//        // given
//
//        // when
//
//        // then
//
//
//    }
//
//}