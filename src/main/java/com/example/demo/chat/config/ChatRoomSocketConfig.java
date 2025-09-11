package com.example.demo.chat.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import com.example.demo.chat.service.ChatRoomService;

//thử nghiệm 
//ws://localhost:8080/chat
//ws://localhost:8080/chat?room=hung
@Configuration // Đánh dấu class này là cấu hình Spring (chứa bean)
public class ChatRoomSocketConfig {

    @Autowired
    private ChatRoomService chatRoomService;
    // ChatRoomService phải implement WebSocketHandler
    // để xử lý kết nối WebSocket (onOpen, onMessage, onClose...)

    @Bean
    public HandlerMapping handlerMapping() {
        // Tạo map giữa URL endpoint và WebSocketHandler
        Map<String, WebSocketHandler> map = Map.of(
                "/chat", chatRoomService // client connect ws://.../chat thì dùng chatRoomService xử lý
        );

        // SimpleUrlHandlerMapping dùng để map request path → handler
        // Order = -1 (ưu tiên cao nhất)
        return new SimpleUrlHandlerMapping(map, -1);
    }

}
