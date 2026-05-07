package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.dao.LeaderboardDao;
import edu.hitsz.factory.EnemySimpleFactory;

public class NormalGame extends Game {

    private static final int BOSS_HP = 20000;
    private static final int BOSS_TRIGGER_SCORE = 3000;

    private double enemyHpMultiplier = 1.00;
    private double enemySpeedMultiplier = 1.00;
    private double enemyBulletSpeedMultiplier = 1.20;

    public NormalGame() {
        super(GameDifficulty.NORMAL);
    }

    NormalGame(LeaderboardDao leaderboardDao, boolean audioEnabled, boolean resultPresentationEnabled) {
        super(GameDifficulty.NORMAL, leaderboardDao, audioEnabled, resultPresentationEnabled);
    }

    @Override
    protected int getInitialEnemyMaxNumber() {
        return 6;
    }

    @Override
    protected double getInitialEnemySpawnCycle() {
        return 20;
    }

    @Override
    protected double getInitialHeroShootCycle() {
        return 6;
    }

    @Override
    protected double getInitialEnemyShootCycle() {
        return 6;
    }

    @Override
    protected boolean canGenerateBoss() {
        return true;
    }

    @Override
    protected int getBossTriggerScore() {
        return BOSS_TRIGGER_SCORE;
    }

    @Override
    protected AbstractAircraft createEnemyAircraft(int gameTime, boolean bossPresent) {
        return EnemySimpleFactory.createEnemy(
                gameTime,
                bossPresent,
                0.55,
                0.22,
                0.13,
                enemyHpMultiplier,
                enemySpeedMultiplier,
                1.00,
                enemyBulletSpeedMultiplier
        );
    }

    @Override
    protected AbstractAircraft createBossEnemy() {
        return EnemySimpleFactory.createBoss(BOSS_HP, 0, 22, scaledBulletSpeed(4));
    }

    @Override
    protected void updateDifficultyAction() {
        if (getTime() > 0 && getTime() % 1000 == 0) {
            enemySpawnCycle = Math.max(12, enemySpawnCycle - 1);
            enemyHpMultiplier += 0.05;
            enemySpeedMultiplier += 0.04;
            enemyBulletSpeedMultiplier += 0.08;
            refreshActiveEnemyShootStrategies(1.00, enemyBulletSpeedMultiplier);
            System.out.printf(
                    "Normal difficulty up: spawnCycle %.0f, enemySpeed x%.2f, enemyHp x%.2f, enemyBulletSpeed x%.2f%n",
                    enemySpawnCycle,
                    enemySpeedMultiplier,
                    enemyHpMultiplier,
                    enemyBulletSpeedMultiplier
            );
        }
    }

    private int scaledBulletSpeed(int baseSpeed) {
        return Math.max(1, (int) Math.round(baseSpeed * enemyBulletSpeedMultiplier));
    }
}
