//package com.madeyepeople.pocketpt.global.chatting;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.SimpMessageType;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//
//@Configuration
//public class SecurityWebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
//        message
////                .simpTypeMatchers(SimpMessageType.CONNECT,
////                        SimpMessageType.HEARTBEAT,
////                        SimpMessageType.UNSUBSCRIBE,
////                        SimpMessageType.DISCONNECT)
////                .permitAll()
////                .anyMessage().authenticated() //or permitAll
////                .simpDestMatchers("/**").authenticated();
//
//                .nullDestMatcher().permitAll()
//                .simpDestMatchers("/pub/**").authenticated()
////                .simpSubscribeDestMatchers("/sub/**").authenticated()
//                .simpSubscribeDestMatchers("/sub/**").permitAll()
//                .anyMessage().denyAll();
//    }
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
//}
