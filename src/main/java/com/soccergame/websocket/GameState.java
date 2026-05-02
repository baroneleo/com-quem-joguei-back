package com.soccergame.websocket;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO representando o estado da sala/jogo enviado via WebSocket.
 * Campos:
 * - id: identificador da sala
 * - country: país da partida
 * - players: lista de usernames
 * - scores: mapa username -> score
 * - current: username do jogador atual
 * - startedAt: timestamp de início (opcional)
 * - winner: username do vencedor (opcional)
 */
public class GameState {

    private String id;
    private String country;
    private List<String> players;
    private Map<String, Integer> scores;
    private String current;
    private Instant startedAt;
    private String winner;

    public GameState() {}

    public GameState(String id, String country, List<String> players, Map<String, Integer> scores, String current, Instant startedAt, String winner) {
        this.id = id;
        this.country = country;
        this.players = players;
        this.scores = scores;
        this.current = current;
        this.startedAt = startedAt;
        this.winner = winner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
