package main.java.com.soccergame.dto.game;

import java.io.Serializable;

/**
 * DTO para eventos enviados do servidor via WebSocket (broadcast / tópico).
 * - type: nome do evento (ex: "player_turn", "game_over", "error")
 * - data: payload arbitrário (pode ser Map, String, objeto DTO, etc.)
 */
public class GameEventDto implements Serializable {

    private String type;
    private Object data;

    public GameEventDto() {}

    public GameEventDto(String type) {
        this.type = type;
    }

    public GameEventDto(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
