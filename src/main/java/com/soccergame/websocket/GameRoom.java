package com.soccergame.websocket;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Modelo de sala de jogo em memória.
 * - suporta até 2 jogadores
 * - controla turnos, pontuação (primeiro a 3 vence)
 * - gerencia nomes já usados na partida
 * - guarda referência a ScheduledFuture do timeout por turno (não agenda por si só)
 */
public class GameRoom {

    private final String id;
    private final String country;
    private final List<String> players = new ArrayList<>(2);
    private final Map<String, Integer> scores = new HashMap<>();
    private final Set<String> usedNames = new HashSet<>(); // normalized lowercase
    private final AtomicInteger turnIndex = new AtomicInteger(0);
    private Instant startedAt;
    private ScheduledFuture<?> turnTimeoutFuture;

    public GameRoom(String id, String country) {
        this.id = id;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public synchronized boolean join(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        if (players.contains(username)) return false;
        if (players.size() >= 2) return false;
        players.add(username);
        scores.putIfAbsent(username, 0);
        return true;
    }

    public synchronized boolean canStart() {
        return players.size() == 2;
    }

    public synchronized void start() {
        if (!canStart()) throw new IllegalStateException("need 2 players to start");
        this.startedAt = Instant.now();
        // ensure scores exist
        for (String p : players) scores.putIfAbsent(p, 0);
        turnIndex.set(0);
        usedNames.clear();
    }

    public synchronized List<String> getPlayers() {
        return List.copyOf(players);
    }

    public synchronized Map<String, Integer> getScores() {
        return Map.copyOf(scores);
    }

    public synchronized String currentPlayer() {
        if (players.isEmpty()) return null;
        int idx = Math.floorMod(turnIndex.get(), players.size());
        return players.get(idx);
    }

    public synchronized boolean isPlayerTurn(String username) {
        return Objects.equals(username, currentPlayer());
    }

    public synchronized void nextTurn() {
        turnIndex.incrementAndGet();
    }

    public synchronized void recordUsedName(String name) {
        if (name != null) usedNames.add(normalize(name));
    }

    public synchronized boolean isNameUsed(String name) {
        if (name == null) return false;
        return usedNames.contains(normalize(name));
    }

    public synchronized void incrementScore(String username) {
        scores.put(username, scores.getOrDefault(username, 0) + 1);
    }

    public synchronized int getScore(String username) {
        return scores.getOrDefault(username, 0);
    }

    public synchronized boolean hasWinner() {
        return scores.values().stream().anyMatch(s -> s >= 3);
    }

    public synchronized Optional<String> winner() {
        return scores.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    public synchronized Map<String, Object> state() {
        return Map.of(
                "id", id,
                "country", country,
                "players", List.copyOf(players),
                "scores", Map.copyOf(scores),
                "current", currentPlayer()
        );
    }

    public synchronized Map<String, Object> summary() {
        return Map.of(
                "id", id,
                "players", List.copyOf(players),
                "country", country
        );
    }

    public synchronized Map<String, Object> winnerSummary() {
        Map<String, Object> m = new HashMap<>();
        m.put("winner", winner().orElse(null));
        m.put("scores", Map.copyOf(scores));
        return m;
    }

    public synchronized void setTurnTimeoutFuture(ScheduledFuture<?> future) {
        cancelTurnTimeout();
        this.turnTimeoutFuture = future;
    }

    public synchronized void cancelTurnTimeout() {
        if (this.turnTimeoutFuture != null && !this.turnTimeoutFuture.isDone()) {
            this.turnTimeoutFuture.cancel(true);
        }
        this.turnTimeoutFuture = null;
    }

    private String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}
