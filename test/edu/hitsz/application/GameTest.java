package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.dao.LeaderboardDao;
import edu.hitsz.model.ScoreRecord;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {

    private final LeaderboardDao noopLeaderboardDao = new LeaderboardDao() {
        @Override
        public void insert(ScoreRecord scoreRecord) {
        }

        @Override
        public List<ScoreRecord> findAll() {
            return Collections.emptyList();
        }

        @Override
        public boolean delete(int index) {
            return false;
        }
    };

    @After
    public void tearDown() {
        HeroAircraft.resetInstance();
    }

    @Test
    public void shouldMarkGameOverWhenHeroHpDropsToZero() {
        HeroAircraft.resetInstance();
        Game game = new Game(GameDifficulty.EXPERT, noopLeaderboardDao, false, false);
        HeroAircraft heroAircraft = game.getHeroAircraftForTest();

        assertFalse(game.isGameOverForTest());

        heroAircraft.decreaseHp(heroAircraft.getHp());
        game.evaluateGameResultForTest();

        assertEquals(0, heroAircraft.getHp());
        assertFalse(heroAircraft.isAlive());
        assertTrue(heroAircraft.notValid());
        assertTrue(game.isGameOverForTest());
    }
}
