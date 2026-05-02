package main.java.com.soccergame.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO para requisição de término de partida via REST.
 * Usado em POST /match/end
 */
public class MatchEndRequest implements Serializable {

    @NotNull(message = "matchId is required")
    private Long matchId;

    @NotBlank(message = "winnerUsername is required")
    private String winnerUsername;

    public MatchEndRequest() { }

    public MatchEndRequest(Long matchId, String winnerUsername) {
        this.matchId = matchId;
        this.winnerUsername = winnerUsername;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }
}
