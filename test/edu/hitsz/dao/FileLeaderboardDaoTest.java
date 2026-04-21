package edu.hitsz.dao;

import edu.hitsz.model.ScoreRecord;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileLeaderboardDaoTest {

    private final Path storagePath = Paths.get("data", "leaderboard-test.txt");

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(storagePath);
    }

    @Test
    public void shouldInsertScoresInDescendingOrder() {
        FileLeaderboardDao dao = new FileLeaderboardDao(storagePath);
        ScoreRecord lowerScore = new ScoreRecord("EASY", "Alice", 120, LocalDateTime.of(2026, 4, 21, 12, 0, 0));
        ScoreRecord higherScore = new ScoreRecord("EXPERT", "Bob", 180, LocalDateTime.of(2026, 4, 21, 12, 1, 0));

        dao.insert(lowerScore);
        dao.insert(higherScore);
        List<ScoreRecord> records = dao.findAll();

        assertEquals(2, records.size());
        assertEquals(higherScore, records.get(0));
        assertEquals(lowerScore, records.get(1));
    }

    @Test
    public void shouldDeleteRecordByDisplayedRank() {
        FileLeaderboardDao dao = new FileLeaderboardDao(storagePath);
        ScoreRecord first = new ScoreRecord("MASTER", "Carol", 300, LocalDateTime.of(2026, 4, 21, 12, 2, 0));
        ScoreRecord second = new ScoreRecord("ADVANCED", "Dave", 150, LocalDateTime.of(2026, 4, 21, 12, 3, 0));

        dao.insert(first);
        dao.insert(second);

        assertTrue(dao.delete(0));

        List<ScoreRecord> records = dao.findAll();
        assertEquals(1, records.size());
        assertEquals(second, records.get(0));
        assertFalse(dao.delete(5));
    }
}
