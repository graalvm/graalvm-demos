/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
