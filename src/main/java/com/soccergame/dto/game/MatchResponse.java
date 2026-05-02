package main.java.com.soccergame.dto.game;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO retornado ao iniciar uma partida (POST /match/start).
 */
public class MatchResponse implements Serializable {

    private Long matchId;
    private String player1;
    private String player2;
    private String country;
    private Instant createdAt;

    public MatchResponse() {}

    public MatchResponse(Long matchId, String player1, String player2, String country, Instant createdAt) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.country = country;
        this.createdAt = createdAt;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
