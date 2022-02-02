package websocket.chat;

/**
 * Service that validates and transforms chat messages.
 */
public interface MessageHandler {
    boolean isValid(String sender, String senderTopic, String message, String receiver, String receiverTopic);

    String createMessage(String sender, String message);
}
