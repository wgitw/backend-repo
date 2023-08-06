package com.madeyepeople.pocketpt.domain.chattingMessageBookmark.controller;

import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.service.ChattingMessageBookmarkService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting/rooms/{chattingRoomId}/messages/bookmark")
@Slf4j
public class ChattingMessageBookmarkController {
    private final SecurityUtil securityUtil;
    private final ChattingMessageBookmarkService chattingMessageBookmarkService;

    // 톡서랍으로 저장
    @PostMapping("/{chattingMessageId}")
    public ResponseEntity<ResultResponse> updateChattingMessageBookmark(@PathVariable Long chattingRoomId, @PathVariable Long chattingMessageId){
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageBookmarkService.createChattingMessageBookmark(chattingRoomId, accountId, chattingMessageId);
        return ResponseEntity.ok(resultResponse);
    }

    // 톡서랍 모아보기
    @GetMapping
    public ResponseEntity<ResultResponse> getChattingMessageBookmarkList(@PathVariable Long chattingRoomId,
                                                                         @PageableDefault(size=10, page=0, sort="chattingMessage", direction = Sort.Direction.DESC) Pageable pageable){
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageBookmarkService.getChattingMessageBookmarkListByRoomAndAccount(chattingRoomId, accountId, pageable);
        return ResponseEntity.ok(resultResponse);
    }

    // 톡서랍에서 삭제
    @DeleteMapping("/{chattingMessageId}")
    public ResponseEntity<ResultResponse> removeChattingMessageBookmark(@PathVariable Long chattingRoomId, @PathVariable Long chattingMessageId){
        Long accountId = securityUtil.getLoginAccountId();
        ResultResponse resultResponse = chattingMessageBookmarkService.removeChattingMessageBookmark(chattingRoomId, accountId, chattingMessageId);
        return ResponseEntity.ok(resultResponse);
    }

}
