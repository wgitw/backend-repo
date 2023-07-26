package com.madeyepeople.pocketpt.global.chatting;

import com.madeyepeople.pocketpt.domain.account.social.JwtUtil;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChattingRoomService chattingRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("============preSend============");
        System.out.println(message);
        System.out.println(channel);
        System.out.println("==========================================");

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // [v]session id 검증 : connect, subscribe, disconnect 다 동일한지
        // [v]session id db에 추가
            // message : 검증
                // 입장 이후면서 퇴장 전이면 채팅 읽음으로 표시할 것
            // [v]disconnection : participant table에 room id가 있으니 추후에 session id로 exit 시간 push 하고 session id는 지우기

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("============CONNECT============");
//            System.out.println(accessor.getUser());
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null) {
                final String accessToken = jwtUtil.extractJwt(accessor);
                Boolean validation = jwtUtil.validateToken(accessToken);
                // TODO: validation false면 connect 중단할 것
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                accessor.setUser(authentication);
            }
            System.out.println("==========================================");
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("============SUBSCRIBE============");
            String accountUsername = accessor.getUser().getName();
//            String accountUsername = accessor.getUser().getName();
//            System.out.println(message.getHeaders().keySet());
            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
            String destination = message.getHeaders().get("simpDestination").toString();
            destination = destination.split("/")[3];
            chattingRoomService.chattingRoomEnter(accountUsername, Long.parseLong(destination), simpSessionId);
//            headerAccessor.getSessionAttributes().put("userUUID", userUUID);
//            headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
            System.out.println("==========================================");
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            System.out.println("============DISCONNECT============");
//            String accountUsername = accessor.getUser().getName();
            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
            chattingRoomService.chattingRoomExit(simpSessionId);
            System.out.println("==========================================");
        }
//        else if (StompCommand.MESSAGE.equals(accessor.getCommand())) {
//            System.out.println("============MESSAGE============");
//            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
//            System.out.println("==========================================");
//        }
        return message;
    }

}