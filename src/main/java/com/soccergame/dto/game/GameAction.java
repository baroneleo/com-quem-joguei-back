package com.soccergame.dto.game;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Mensagem enviada pelo cliente para submeter uma resposta/jogada via WebSocket.
 * Mapea para destino /app/submit_answer
 */
public class GameAction implements Serializable {

    @NotBlank
    private String roomId;

    @NotBlank
    private String username;

    @NotBlank
    private String answer;

    public GameAction() {}

    public GameAction(String roomId, String username, String answer) {
        this.roomId = roomId;
        this.username = username;
        this.answer = answer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
