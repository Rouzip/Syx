package com.CSU.Syx.control;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @author Rouzip
 * @date 2017.11.6
 */
@Component
public class SocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage("Hello"));
    }

}