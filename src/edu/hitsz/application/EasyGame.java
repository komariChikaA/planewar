package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.dao.LeaderboardDao;
import edu.hitsz.factory.EnemySimpleFactory;

public class EasyGame extends Game {

    public EasyGame() {
        super(GameDifficulty.EASY);
    }

    EasyGame(LeaderboardDao leaderboardDao, boolean audioEnabled, boolean resultPresentationEnabled) {
        super(GameDifficulty.EASY, leaderboardDao, audioEnabled, resultPresentationEnabled);
    }

    @Override
    protected int getInitialEnemyMaxNumber() {
        return 5;
    }

    @Override
    protected double getInitialEnemySpawnCycle() {
        return 24;
    }

    @Override
    protected double getInitialHeroShootCycle() {
        return 6;
    }

    @Override
    protected double getInitialEnemyShootCycle() {
        return 8;
    }

    @Override
    protected boolean canGenerateBoss() {
        return false;
    }

    @Override
    protected int getBossTriggerScore() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected AbstractAircraft createEnemyAircraft(int gameTime, boolean bossPresent) {
        return EnemySimpleFactory.createEnemy(gameTime, bossPresent, 0.70, 0.20, 0.10, 0.80, 0.75, 0.70, 0.85);
    }

    @Override
    protected AbstractAircraft createBossEnemy() {
        return null;
    }
}
