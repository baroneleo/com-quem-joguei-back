package com.soccergame.dto.game;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO usado em eventos de WebSocket relacionados a salas:
 * - create_room: espera hostUsername, country
 * - join_room: espera roomId, joinUsername
 * - start_game: espera roomId, hostUsername (opcional)
 */
public class RoomEvent implements Serializable {

    private String roomId;

    // quando criar sala
    private String hostUsername;

    // quando alguém entra na sala
    private String joinUsername;

    // país da partida (opcional dependendo do fluxo)
    private String country;

    public RoomEvent() {}

    public RoomEvent(String roomId, String hostUsername, String joinUsername, String country) {
        this.roomId = roomId;
        this.hostUsername = hostUsername;
        this.joinUsername = joinUsername;
        this.country = country;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getJoinUsername() {
        return joinUsername;
    }

    public void setJoinUsername(String joinUsername) {
        this.joinUsername = joinUsername;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
