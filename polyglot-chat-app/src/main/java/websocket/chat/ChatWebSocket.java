package websocket.chat;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;

import java.util.function.Predicate;

@ServerWebSocket("/ws/chat/{topic}/{username}")
public class ChatWebSocket {
    private final WebSocketBroadcaster broadcaster;
    private final MessageHandler messageHandler;

    public ChatWebSocket(WebSocketBroadcaster broadcaster, MessageHandler messageHandler) {
        this.broadcaster = broadcaster;
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public Publisher<String> onOpen(String topic, String username, WebSocketSession session) {
        String msg = messageHandler.createMessage(username, "Joined!");
        return broadcaster.broadcast(msg, isValid(username, topic, msg));
    }

    @OnMessage
    public Publisher<String> onMessage(
            String topic,
            String username,
            String message,
            WebSocketSession session) {
        String msg = messageHandler.createMessage(username, message);
        return broadcaster.broadcast(msg, isValid(username, topic, message));
    }

    @OnClose
    public Publisher<String> onClose(
            String topic,
            String username,
            WebSocketSession session) {
        String msg = messageHandler.createMessage(username, "Disconnected!");
        return broadcaster.broadcast(msg, isValid(username, topic, msg));
    }

    private Predicate<WebSocketSession> isValid(String sender, String senderTopic, String message) {
        return s -> {
            String receiver = s.getUriVariables().get("username", String.class, null);
            String receiverTopic = s.getUriVariables().get("topic", String.class, null);
            return messageHandler.isValid(sender, senderTopic, message, receiver, receiverTopic);
        };
    }
}
