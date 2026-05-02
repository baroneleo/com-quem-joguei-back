package com.soccergame.controller;

import com.soccergame.dto.game.GameEventDto;
import com.soccergame.dto.game.GameAction;
import com.soccergame.dto.game.RoomEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * Controller STOMP para eventos do jogo.
 * Mensagens de entrada (do cliente) mapeadas para:
 * - /app/create_room
 * - /app/join_room
 * - /app/start_game
 * - /app/submit_answer
 *
 * Emite atualizações no tópico /topic/game/{roomId}
 *
 * Nota: implementação em memória, simples. Para produção, extraia lógica para um service.
 */
@Controller
public class GameWebSocketController {

    private final SimpMessagingTemplate template;
    private final Logger log = LoggerFactory.getLogger(GameWebSocketController.class);

    // rooms in-memory
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public GameWebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/create_room")
    public void createRoom(@Payload RoomEvent event, @Header(name = "simpSessionId", required = false) String sessionId) {
        String id = UUID.randomUUID().toString();
        Room r = new Room(id, event.getHostUsername(), event.getCountry());
        r.join(event.getHostUsername());
        rooms.put(id, r);
        log.info("Room created id={} host={} country={}", id, event.getHostUsername(), event.getCountry());
        // send immediate ack to the creating user (by username). If you prefer session-based, adapt accordingly.
        template.convertAndSendToUser(event.getHostUsername(), "/queue/reply", new RoomCreatedResponse(id));
    }

    @MessageMapping("/join_room")
    public void joinRoom(@Payload RoomEvent event) {
        Room r = rooms.get(event.getRoomId());
        if (r == null) {
            sendToRoom(event.getRoomId(), new GameEventDto("error", "Room not found"));
            return;
        }
        boolean joined = r.join(event.getJoinUsername());
        if (!joined) {
            template.convertAndSendToUser(event.getJoinUsername(), "/queue/reply", new GameEventDto("error", "Unable to join"));
            return;
        }
        sendToRoom(r.id, new GameEventDto("room_update", r.summary()));
    }

    @MessageMapping("/start_game")
    public void startGame(@Payload RoomEvent event) {
        Room r = rooms.get(event.getRoomId());
        if (r == null) {
            sendToRoom(event.getRoomId(), new GameEventDto("error", "Room not found"));
            return;
        }
        if (!r.canStart()) {
            sendToRoom(r.id, new GameEventDto("error", "Need 2 players to start"));
            return;
        }
        r.start();
        sendToRoom(r.id, new GameEventDto("game_started", r.state()));
        scheduleTurnTimeout(r);
    }

    @MessageMapping("/submit_answer")
    public void submitAnswer(@Payload GameAction action) {
        Room r = rooms.get(action.getRoomId());
        if (r == null) {
            sendToRoom(action.getRoomId(), new GameEventDto("error", "Room not found"));
            return;
        }
        // validate turn
        if (!r.isPlayerTurn(action.getUsername())) {
            template.convertAndSendToUser(action.getUsername(), "/queue/reply", new GameEventDto("error", "Not your turn"));
            return;
        }
        // simple validation: non-empty, not repeated
        String answer = Optional.ofNullable(action.getAnswer()).orElse("").trim();
        if (answer.isEmpty() || r.isNameUsed(answer)) {
            template.convertAndSendToUser(action.getUsername(), "/queue/reply", new GameEventDto("invalid_move", "Invalid or repeated name"));
            return;
        }
        // Mock validation of "played in same club" - here accept everything
        r.recordUsedName(answer);
        r.incrementScore(action.getUsername());
        sendToRoom(r.id, new GameEventDto("player_scored", Map.of("player", action.getUsername(), "score", r.getScore(action.getUsername()))));

        // check for win (first to 3)
        if (r.hasWinner()) {
            sendToRoom(r.id, new GameEventDto("game_over", r.winnerSummary()));
            // optionally persist match result via MatchService (not wired here)
            rooms.remove(r.id);
            r.cancelTimeout();
            return;
        }

        // next turn
        r.nextTurn();
        sendToRoom(r.id, new GameEventDto("player_turn", r.state()));
        scheduleTurnTimeout(r);
    }

    // helper to schedule turn timeout
    private void scheduleTurnTimeout(Room r) {
        // cancel previous if exists
        r.cancelTimeout();
        Runnable task = () -> {
            if (!rooms.containsKey(r.id)) return;
            String timedOutPlayer = r.currentPlayer();
            r.nextTurn();
            sendToRoom(r.id, new GameEventDto("timer_update", Map.of("timedOut", timedOutPlayer, "next", r.currentPlayer())));
            // if someone reached win because of default (optional) check
            if (r.hasWinner()) {
                sendToRoom(r.id, new GameEventDto("game_over", r.winnerSummary()));
                rooms.remove(r.id);
                r.cancelTimeout();
            } else {
                sendToRoom(r.id, new GameEventDto("player_turn", r.state()));
                scheduleTurnTimeout(r); // schedule next
            }
        };
        ScheduledFuture<?> future = scheduler.schedule(task, 20, TimeUnit.SECONDS);
        r.setTimeoutFuture(future);
    }

    private void sendToRoom(String roomId, GameEventDto payload) {
        template.convertAndSend("/topic/game/" + roomId, payload);
    }

    // --- Simple response / models used locally. Consider moving to dto/ packages ---

    public static record RoomCreatedResponse(String roomId) {}

    // Minimal in-memory Room model
    private static class Room {
        final String id;
        final String country;
        final List<String> players = new ArrayList<>();
        final Map<String, Integer> scores = new HashMap<>();
        final Set<String> usedNames = new HashSet<>();
        int turnIndex = 0;
        Instant startedAt;
        ScheduledFuture<?> timeoutFuture;

        Room(String id, String host, String country) {
            this.id = id;
            this.country = country;
            players.add(host);
        }

        boolean join(String username) {
            if (players.contains(username) || players.size() >= 2) return false;
            players.add(username);
            scores.put(username, 0);
            return true;
        }

        boolean canStart() {
            return players.size() == 2;
        }

        void start() {
            this.startedAt = Instant.now();
            players.forEach(p -> scores.putIfAbsent(p, 0));
            turnIndex = 0;
            usedNames.clear();
        }

        boolean isPlayerTurn(String username) {
            if (players.isEmpty()) return false;
            int idx = Math.floorMod(turnIndex, players.size());
            return players.get(idx).equals(username);
        }

        String currentPlayer() {
            if (players.isEmpty()) return null;
            int idx = Math.floorMod(turnIndex, players.size());
            return players.get(idx);
        }

        void nextTurn() {
            turnIndex = (turnIndex + 1) % Math.max(1, players.size());
        }

        void incrementScore(String username) {
            scores.put(username, scores.getOrDefault(username, 0) + 1);
        }

        int getScore(String username) {
            return scores.getOrDefault(username, 0);
        }

        boolean hasWinner() {
            return scores.values().stream().anyMatch(s -> s >= 3);
        }

        Map<String, Object> winnerSummary() {
            return Map.of("winner", scores.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(null),
                          "scores", Map.copyOf(scores));
        }

        Map<String, Object> state() {
            return Map.of(
                    "id", id,
                    "country", country,
                    "players", List.copyOf(players),
                    "scores", Map.copyOf(scores),
                    "current", currentPlayer(),
                    "startedAt", startedAt
            );
        }

        Map<String, Object> summary() {
            return Map.of(
                    "id", id,
                    "players", List.copyOf(players),
                    "country", country,
                    "startedAt", startedAt
            );
        }

        void setTimeoutFuture(ScheduledFuture<?> f) {
            cancelTimeout();
            this.timeoutFuture = f;
        }

        void cancelTimeout() {
            if (this.timeoutFuture != null && !this.timeoutFuture.isDone()) this.timeoutFuture.cancel(true);
            this.timeoutFuture = null;
        }

        void recordUsedName(String name) {
            if (name != null) usedNames.add(normalize(name));
        }

        boolean isNameUsed(String name) {
            if (name == null) return false;
            return usedNames.contains(normalize(name));
        }

        private String normalize(String s) {
            return s == null ? null : s.trim().toLowerCase();
        }
    }
}
