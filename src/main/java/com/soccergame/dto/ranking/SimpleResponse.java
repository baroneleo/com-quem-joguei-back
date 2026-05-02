package main.java.com.soccergame.dto.ranking;

import java.io.Serializable;

/**
 * DTO genérico para respostas simples de endpoints REST.
 */
public class SimpleResponse implements Serializable {

    private String message;

    public SimpleResponse() {}

    public SimpleResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
