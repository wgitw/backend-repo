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
        // TODO: file 가져오는 기준 정할 것
        ResultResponse resultResponse = chattingRoomService.getChattingFileListByRoom(chattingRoomId);
        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ResultResponse> getChattingRoomListByUser(@PathVariable Long accountId) {
        ResultResponse resultResponse = chattingRoomService.getChattingRoomListByUser(accountId);
        return ResponseEntity.ok(resultResponse);
    }

}
