package com.madeyepeople.pocketpt.domain.chattingMessage.controller;

import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingFileCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.dto.request.ChattingMessageCreateRequest;
import com.madeyepeople.pocketpt.domain.chattingMessage.service.ChattingMessageService;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting")
public class ChattingMessageController {
    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;

    private final ChattingMessageService chattingMessageService;

    @MessageMapping("/chatting-message") // MessageMapping은 RequestMapping의 영향을 받지 않는 듯함
    public void sendChattingMessage(ChattingMessageCreateRequest chattingMessageCreateRequest) {
        Map<String, Object> map = chattingMessageService.createChattingMessage(chattingMessageCreateRequest);
        ResultResponse resultResponse = (ResultResponse) map.get("resultResponse");
        Long chattingRoomId = (Long) map.get("chattingRoomId");
        template.convertAndSend("/sub/channel/" + chattingRoomId, resultResponse);
    }

    @PostMapping("/file")
    public ResponseEntity<ResultResponse> createChattingFile(@ModelAttribute ChattingFileCreateRequest chattingRoomCreateRequest) {
        ResultResponse resultResponse = chattingMessageService.createChattingFile(chattingRoomCreateRequest);
        return ResponseEntity.ok(resultResponse);
    }

//    @PostMapping("/upload")
//    public FileUploadDto uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId")String roomId){
//
//        FileUploadDto fileReq = fileService.uploadFile(file, UUID.randomUUID().toString(), roomId);
//        log.info("최종 upload Data {}", fileReq);
//
//        // fileReq 객체 리턴
//        return fileReq;
//    }
//
//    // get 으로 요청이 오면 아래 download 메서드를 실행한다.
//    // fileName 과 파라미터로 넘어온 fileDir 을 getObject 메서드에 매개변수로 넣는다.
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<byte[]> download(@PathVariable String fileName, @RequestParam("fileDir")String fileDir){
//        log.info("fileDir : fileName [{} : {}]", fileDir, fileName);
//        try {
//            // 변환된 byte, httpHeader 와 HttpStatus 가 포함된 ResponseEntity 객체를 return 한다.
//            return fileService.getObject(fileDir, fileName);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
