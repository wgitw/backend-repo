package com.madeyepeople.pocketpt.domain.chattingRoom.controller;

import com.madeyepeople.pocketpt.domain.chattingRoom.dto.request.ChattingRoomCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatting/rooms")
public class ChattingRoomController {

    private final SecurityUtil securityUtil;
    private final ChattingRoomService chattingRoomService;

    //  채팅방 만들기
    @PostMapping
    public ResponseEntity<ResultResponse> createChattingRoom(@RequestBody ChattingRoomCreateRequest chattingRoomCreateRequest,
                                                             RedirectAttributes rttr) {
        Long hostAccountId = securityUtil.getLoginUsername();
        ResultResponse resultResponse = chattingRoomService.createChattingRoom(hostAccountId, chattingRoomCreateRequest);
        rttr.addFlashAttribute("roomName", "roomName");
        return ResponseEntity.ok(resultResponse);
    }

    // 채팅방 리스트 가져오기 - 회원 ID 기준
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<ResultResponse> getChattingRoomListByUser(@PathVariable Long accountId, Pageable pageable) {
        // TODO: 정렬 기준 - 가장 최근 채팅 메시지가 온 방으로 정렬하도록 수정할 것
        ResultResponse resultResponse = chattingRoomService.getChattingRoomListByUser(accountId, pageable);
        return ResponseEntity.ok(resultResponse);
    }

}
