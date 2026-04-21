package edu.hitsz.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ScoreRecord)) {
            return false;
        }
        ScoreRecord that = (ScoreRecord) other;
        return score == that.score
                && Objects.equals(difficulty, that.difficulty)
                && Objects.equals(playerName, that.playerName)
                && Objects.equals(playedAt, that.playedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(difficulty, playerName, score, playedAt);
    }
}
