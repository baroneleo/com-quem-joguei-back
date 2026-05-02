package com.soccergame.dto;

import java.io.Serializable;

public class SimpleResponse implements Serializable {
    private String message;
    public SimpleResponse() {}
    public SimpleResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
