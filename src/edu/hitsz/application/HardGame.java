package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.dao.LeaderboardDao;
import edu.hitsz.factory.EnemySimpleFactory;

public class HardGame extends Game {

    private static final int BASE_BOSS_HP = 24000;
    private static final int BOSS_TRIGGER_SCORE = 3000;

    private double enemyHpMultiplier = 1.20;
    private double enemySpeedMultiplier = 1.15;
    private double enemyBulletPowerMultiplier = 1.20;
    private double enemyBulletSpeedMultiplier = 1.55;
    private int bossSummonCount = 0;

    public HardGame() {
        super(GameDifficulty.HARD);
    }

    HardGame(LeaderboardDao leaderboardDao, boolean audioEnabled, boolean resultPresentationEnabled) {
        super(GameDifficulty.HARD, leaderboardDao, audioEnabled, resultPresentationEnabled);
    }

    @Override
    protected int getInitialEnemyMaxNumber() {
        return 7;
    }

    @Override
    protected double getInitialEnemySpawnCycle() {
        return 18;
    }

    @Override
    protected double getInitialHeroShootCycle() {
        return 6;
    }

    @Override
    protected double getInitialEnemyShootCycle() {
        return 5;
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
                0.45,
                0.25,
                0.17,
                enemyHpMultiplier,
                enemySpeedMultiplier,
                enemyBulletPowerMultiplier,
                enemyBulletSpeedMultiplier
        );
    }

    @Override
    protected AbstractAircraft createBossEnemy() {
        bossSummonCount++;
        int bossHp = BASE_BOSS_HP + (bossSummonCount - 1) * 5000;
        return EnemySimpleFactory.createBoss(
                bossHp,
                3,
                (int) Math.round(22 * enemyBulletPowerMultiplier),
                scaledBulletSpeed(4)
        );
    }

    @Override
    protected void updateDifficultyAction() {
        if (getTime() > 0 && getTime() % 800 == 0) {
            enemySpawnCycle = Math.max(10, enemySpawnCycle - 1);
            enemyHpMultiplier += 0.08;
            enemySpeedMultiplier += 0.05;
            enemyBulletPowerMultiplier += 0.05;
            enemyBulletSpeedMultiplier += 0.10;
            heroShootCycle = Math.max(3, heroShootCycle - 0.25);
            enemyShootCycle = Math.max(3, enemyShootCycle - 0.25);
            refreshActiveEnemyShootStrategies(enemyBulletPowerMultiplier, enemyBulletSpeedMultiplier);
            System.out.printf(
                    "Hard difficulty up: spawnCycle %.0f, enemySpeed x%.2f, enemyHp x%.2f, enemyBulletSpeed x%.2f, heroShootCycle %.2f, enemyShootCycle %.2f%n",
                    enemySpawnCycle,
                    enemySpeedMultiplier,
                    enemyHpMultiplier,
                    enemyBulletSpeedMultiplier,
                    heroShootCycle,
                    enemyShootCycle
            );
        }
    }

    private int scaledBulletSpeed(int baseSpeed) {
        return Math.max(1, (int) Math.round(baseSpeed * enemyBulletSpeedMultiplier));
    }
}
