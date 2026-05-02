package com.soccergame.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler WebSocket de baixo nível (opcional se você usa STOMP/MessageMapping).
 * Fornece um exemplo mínimo de gerenciamento de sessões e encaminhamento de mensagens brutas.
 *
 * Nota: se você estiver usando apenas STOMP com GameWebSocketController, este handler pode ficar sem uso.
 */
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(GameWebSocketHandler.class);

    // map sessionId -> WebSocketSession
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // map sessionId -> roomId (simples associação)
    private final Map<String, String> sessionRoom = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        log.info("WS connected: sessionId={}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.debug("Received WS message from {}: {}", session.getId(), payload);

        // Exemplo mínimo: ecoa a mensagem de volta
        try {
            session.sendMessage(new TextMessage("{\"type\":\"echo\",\"payload\":" + escapeJson(payload) + "}"));
        } catch (IllegalStateException ex) {
            log.warn("Failed to send message to session {}: {}", session.getId(), ex.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.warn("Transport error sessionId={} : {}", session.getId(), exception.getMessage());
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        sessionRoom.remove(session.getId());
        log.info("WS disconnected: sessionId={} status={}", session.getId(), status);
    }

    /**
     * Associa uma sessão a uma sala (usado se decidir integrar este handler com GameRoom).
     */
    public void assignSessionToRoom(String sessionId, String roomId) {
        sessionRoom.put(sessionId, roomId);
    }

    private String escapeJson(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
}
