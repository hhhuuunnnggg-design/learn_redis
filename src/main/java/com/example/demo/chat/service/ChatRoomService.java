package com.example.demo.chat.service;

import java.net.URI;

import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatRoomService implements WebSocketHandler {

    @Autowired
    private RedissonReactiveClient client;
    // Redisson client (Reactive) → dùng để publish/subscribe message & lưu lịch sử
    // vào Redis

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        // Lấy tên phòng chat từ query param ?room=abc (nếu không có → "default")
        String room = getChatRoomName(webSocketSession);

        // Redis topic để publish/subscribe tin nhắn
        RTopicReactive topic = this.client.getTopic(room, StringCodec.INSTANCE);

        // Redis list để lưu history tin nhắn
        RListReactive<String> list = this.client.getList("history:" + room, StringCodec.INSTANCE);

        // ----------------- SUBSCRIBER -----------------
        // Khi client gửi tin nhắn qua WebSocket:
        webSocketSession.receive() // lắng nghe tin nhắn từ client
                .map(WebSocketMessage::getPayloadAsText) // lấy text từ WebSocketMessage
                .flatMap(msg ->
                // lưu message vào Redis list (history) rồi publish tới topic
                list.add(msg).then(topic.publish(msg)))
                .doOnError(System.out::println) // log lỗi nếu có
                .doFinally(s -> System.out.println("Subscriber finally " + s)) // cleanup
                .subscribe(); // kích hoạt luồng subscriber

        // ----------------- PUBLISHER -----------------
        // Flux để gửi tin nhắn ngược lại cho client
        Flux<WebSocketMessage> flux = topic.getMessages(String.class) // nhận message từ Redis topic
                .startWith(list.iterator()) // khi user vừa join, gửi lại toàn bộ history trước
                .map(webSocketSession::textMessage) // map message string → WebSocketMessage
                .doOnError(System.out::println) // log lỗi nếu có
                .doFinally(s -> System.out.println("publisher finally " + s)); // cleanup

        // Gửi dữ liệu (flux) về client
        return webSocketSession.send(flux);
    }

    // Helper: lấy tên phòng từ query param
    private String getChatRoomName(WebSocketSession socketSession) {
        URI uri = socketSession.getHandshakeInfo().getUri();
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room", "default"); // nếu không có param thì default
    }

}
