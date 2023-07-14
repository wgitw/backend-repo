package com.madeyepeople.pocketpt.domain.chattingRoom.controller;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatting/rooms")
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ResultResponse> createChattingRoom(@RequestBody ChattingRoomCreateRequest chattingRoomCreateRequest, RedirectAttributes rttr) {
        ResultResponse resultResponse = chattingRoomService.createChattingRoom(chattingRoomCreateRequest);
        rttr.addFlashAttribute("roomName", "roomName");
        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("/{chattingRoomId}/messages")
    public  ResponseEntity<ResultResponse> getChattingMessageListByRoom(@PathVariable Long chattingRoomId) {
        // TODO: message 가져오는 기준 정할 것
        ResultResponse resultResponse = chattingRoomService.getChattingMessageListByRoom(chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("/{chattingRoomId}/files")
    public  ResponseEntity<ResultResponse> getChattingFileListByRoom(@PathVariable Long chattingRoomId) {
        // TODO: message 가져오는 기준 정할 것
        ResultResponse resultResponse = chattingRoomService.getChattingFileListByRoom(chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/rooms/{chattingRoomId}")
    public String roomDetail(@PathVariable Long chattingRoomId){

        log.info("chattingRoomId {}", chattingRoomId);

        // principalDetails 가 null 이 아니라면 로그인 된 상태!!
//        if (principalDetails != null) {
//            // 세션에서 로그인 유저 정보를 가져옴
//            model.addAttribute("user", principalDetails.getUser());
//        }

        return "";
//
//
//        model.addAttribute("room", chatRepository.findRoomById(roomId));
//        return "chatroom";
    }

    // 채팅 리스트 화면
    // 스프링 시큐리티의 로그인 유저 정보는 Security 세션의 PrincipalDetails 안에 담긴다
    // 정확히는 PrincipalDetails 안에 ChatUser 객체가 담기고, 이것을 가져오면 된다.
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ResultResponse> getChattingRoomListByUser(@PathVariable Long accountId) {
        ResultResponse resultResponse = chattingRoomService.getChattingRoomListByUser(accountId);
        return ResponseEntity.ok(resultResponse);
    }

}
