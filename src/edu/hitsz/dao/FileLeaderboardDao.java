package edu.hitsz.dao;

import edu.hitsz.model.ScoreRecord;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileLeaderboardDao implements LeaderboardDao {

    private static final Comparator<ScoreRecord> SCORE_COMPARATOR =
            Comparator.comparingInt(ScoreRecord::getScore)
                    .reversed()
                    .thenComparing(ScoreRecord::getPlayedAt, Comparator.reverseOrder());

    private final Path storagePath;

    public FileLeaderboardDao(Path storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public synchronized void insert(ScoreRecord scoreRecord) {
        List<ScoreRecord> records = new ArrayList<>(findAll());
        records.add(scoreRecord);
        records.sort(SCORE_COMPARATOR);
        writeAll(records);
    }

    @Override
    public synchronized List<ScoreRecord> findAll() {
        ensureStorageFile();
        try {
            List<ScoreRecord> records = new ArrayList<>();
            for (String line : Files.readAllLines(storagePath, StandardCharsets.UTF_8)) {
                ScoreRecord scoreRecord = parseLine(line);
                if (scoreRecord != null) {
                    records.add(scoreRecord);
                }
            }
            records.sort(SCORE_COMPARATOR);
            return records;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read leaderboard file.", e);
        }
    }

    private void writeAll(List<ScoreRecord> records) {
        ensureStorageFile();
        List<String> lines = new ArrayList<>();
        for (ScoreRecord record : records) {
            lines.add(formatLine(record));
        }
        try {
            Files.write(
                    storagePath,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write leaderboard file.", e);
        }
    }

    private void ensureStorageFile() {
        try {
            Path parent = storagePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(storagePath)) {
                Files.createFile(storagePath);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to initialize leaderboard file.", e);
        }
    }

    private ScoreRecord parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\t", 4);
        if (parts.length != 3 && parts.length != 4) {
            return null;
        }
        try {
            String difficulty = parts.length == 4 ? parts[0] : "EXPERT";
            String playerName = parts.length == 4 ? parts[1] : parts[0];
            int score = Integer.parseInt(parts.length == 4 ? parts[2] : parts[1]);
            LocalDateTime playedAt = ScoreRecord.parsePlayedAt(parts.length == 4 ? parts[3] : parts[2]);
            return new ScoreRecord(difficulty, playerName, score, playedAt);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String formatLine(ScoreRecord record) {
        return record.getDifficulty() + "\t"
                + record.getPlayerName() + "\t"
                + record.getScore() + "\t"
                + record.formatPlayedAt();
    }
}
