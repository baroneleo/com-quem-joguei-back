package main.java.com.soccergame.dto.game;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO para iniciar partida via REST (POST /match/start).
 * Equivalente a MatchRequest — separado para semântica.
 */
public class MatchStartRequest implements Serializable {

    @NotBlank(message = "player1 is required")
    private String player1;

    @NotBlank(message = "player2 is required")
    private String player2;

    @NotBlank(message = "country is required")
    private String country;

    public MatchStartRequest() {}

    public MatchStartRequest(String player1, String player2, String country) {
        this.player1 = player1;
        this.player2 = player2;
        this.country = country;
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
}
