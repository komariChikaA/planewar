package edu.hitsz.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreRecord {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String difficulty;
    private final String playerName;
    private final int score;
    private final LocalDateTime playedAt;

    public ScoreRecord(String difficulty, String playerName, int score, LocalDateTime playedAt) {
        this.difficulty = difficulty;
        this.playerName = playerName;
        this.score = score;
        this.playedAt = playedAt;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public String formatPlayedAt() {
        return playedAt.format(FORMATTER);
    }

    public static LocalDateTime parsePlayedAt(String value) {
        return LocalDateTime.parse(value, FORMATTER);
    }
}
