package com.soccergame.dto.ranking;

import java.io.Serializable;

/**
 * DTO para retorno de ranking (global ou por país).
 */
public class RankingDto implements Serializable {

    private String username;
    private int wins;
    private int losses;
    private int score; // wins * 3 - losses

    public RankingDto() {}

    public RankingDto(String username, int wins, int losses, int score) {
        this.username = username;
        this.wins = wins;
        this.losses = losses;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
