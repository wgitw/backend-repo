package com.madeyepeople.pocketpt.global.chatting;

import com.madeyepeople.pocketpt.domain.account.social.JwtUtil;
import com.madeyepeople.pocketpt.domain.chattingRoom.service.ChattingRoomService;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.ErrorResponse;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.security.Principal;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChattingRoomService chattingRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("=======================");
        log.info("WS-STOMP: [PRESEND] START");
        log.info("WS-STOMP: [PRESEND] Message>> " + message.toString());
        log.info("WS-STOMP: [PRESEND] MessageChannel>> " + channel.toString());
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // [v]session id 검증 : connect, subscribe, disconnect 다 동일한지
        // [v]session id db에 추가
            // [v]message : 검증
                // 입장 이후면서 퇴장 전이면 채팅 읽음으로 표시할 것
            // [v]disconnection : participant table에 room id가 있으니 추후에 session id로 exit 시간 push 하고 session id는 지우기

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("WS-STOMP: [CONNECT] START");
            log.info("WS-STOMP: [CONNECT] StompHeaderAccessor>> " + accessor.toString());
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null) {
                final String accessToken = jwtUtil.extractJwt(accessor);

                try {
                    jwtUtil.validateToken(accessToken);
                } catch (JwtException e) {
                    log.error("WS-STOMP: [CONNECT] JwtException>> {}", e.getMessage());
                    StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.JWT_VALIDATION_ERROR);
                    String errorMessage;
                    try {
                        errorMessage = errorResponse.toJSonString();
                    } catch (Exception ex) {
                        log.error("WS-STOMP: [CONNECT] json to string converter error");
                        errorMessage = "알 수 없는 에러";
                    }
                    headerAccessor.setMessage(errorMessage);
                    channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
                } catch (Exception e) {
                    log.error("WS-STOMP: [CONNECT] Exception>> {}", e.getMessage());
                }

                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                accessor.setUser(authentication);
            }
            log.info("WS-STOMP: [CONNECT] SUCCESS");
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("WS-STOMP: [SUBSCRIBE] START");
            log.info("WS-STOMP: [SUBSCRIBE] StompHeaderAccessor>> " + accessor.toString());
            Principal user = accessor.getUser();
            if(user == null) {
                log.error("WS-STOMP: [SUBSCRIBE] user is null");
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                headerAccessor.setMessage("user is null");
                channel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
            }
            String accountUsername = accessor.getUser().getName();
            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
            String simpDestination = message.getHeaders().get("simpDestination").toString();
            String destination = accessor.getNativeHeader("destination").toString();

            String str = destination.split("/")[2];
            if (str.equals("channel")) {
                // 채팅방 입장
                simpDestination = simpDestination.split("/")[3];
                chattingRoomService.chattingRoomEnter(accountUsername, Long.parseLong(simpDestination), simpSessionId);
                log.info("WS-STOMP: [SUBSCRIBE] enter the "+ simpDestination +"chatting room");
            } else if (str.equals("accounts")) {
                // 채팅방 리스트 입장
                String username = accessor.getUser().getName();
                // do nothing
                log.info("WS-STOMP: [SUBSCRIBE] enter the chatting room list for " + username);
            }
            log.info("WS-STOMP: [SUBSCRIBE] SUCCESS");

            // headerAccessor.getSessionAttributes().put("userUUID", userUUID);
            // headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("WS-STOMP: [DISCONNECT] START");
            log.info("WS-STOMP: [DISCONNECT] StompHeaderAccessor>> " + accessor.toString());
            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
            chattingRoomService.chattingRoomExit(simpSessionId);
            log.info("WS-STOMP: [DISCONNECT] SUCCESS");
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("WS-STOMP: [SEND] START");
            log.info("WS-STOMP: [SEND] StompHeaderAccessor>> " + accessor.toString());
            String simpSessionId = message.getHeaders().get("simpSessionId").toString();
            log.info("WS-STOMP: [SEND] SUCCESS");
        } else {
            String accessorCommand = accessor.getCommand().toString();
            log.info("WS-STOMP: [" + accessorCommand + "] START");
            log.info("WS-STOMP: [" + accessorCommand + "] StompHeaderAccessor>> " + accessor.toString());
            log.info("WS-STOMP: [" + accessorCommand + "] SUCCESS");
        }
        log.info("WS-STOMP: [PRESEND] END");
        log.info("=======================\n\n");
        return message;
    }
}
