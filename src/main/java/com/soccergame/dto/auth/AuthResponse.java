package main.java.com.soccergame.dto.auth;

import java.io.Serializable;

/**
 * DTO retornado após login contendo o JWT.
 */
public class AuthResponse implements Serializable {
    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
