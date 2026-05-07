package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.application.GameDifficulty;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.CircleShootStrategy;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.NoShootStrategy;
import edu.hitsz.strategy.ParallelShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.Random;

public class EnemySimpleFactory {

    private static final Random RANDOM = new Random();
    private static final int HP_DOUBLE_INTERVAL = 1500;
    private static final int BOSS_HP = 20000;
    private static final int ELITE_DIRECT_BULLET_SPEED = 9;

    private static final EnemyFactory MOB_FACTORY = new MobEnemyFactory();
    private static final EnemyFactory ELITE_FACTORY = new EliteEnemyFactory();
    private static final EnemyFactory ELITE_PLUS_FACTORY = new ElitePlusEnemyFactory();
    private static final EnemyFactory ELITE_PRO_FACTORY = new EliteProEnemyFactory();
    private static final EnemyFactory BOSS_FACTORY = new BossEnemyFactory();

    private EnemySimpleFactory() {
    }

    public static EnemyAircraft createBoss(GameDifficulty difficulty) {
        int speedX = difficulty.isBossMovable() ? 3 : 0;
        return createBoss(difficulty.scaleEnemyHp(BOSS_HP), speedX, difficulty.scaleEnemyBulletPower(22), 4);
    }

    public static EnemyAircraft createBoss(int hp, int speedX, int bulletPower, int bulletSpeed) {
        return BOSS_FACTORY.createEnemy(
                Main.WINDOW_WIDTH / 2,
                0,
                speedX,
                0,
                hp,
                bulletPower,
                bulletSpeed
        );
    }

    public static EnemyAircraft createEnemy(int gameTime, boolean bossPresent, GameDifficulty difficulty) {
        return createEnemy(
                gameTime,
                bossPresent,
                0.50,
                0.20,
                0.15,
                difficulty.getEnemyHpMultiplier(),
                difficulty.getEnemySpeedMultiplier(),
                difficulty.getEnemyBulletPowerMultiplier(),
                1.00
        );
    }

    public static EnemyAircraft createEnemy(
            int gameTime,
            boolean bossPresent,
            double mobProbability,
            double eliteProbability,
            double elitePlusProbability,
            double enemyHpMultiplier,
            double enemySpeedMultiplier,
            double enemyBulletPowerMultiplier,
            double enemyBulletSpeedMultiplier
    ) {
        int type = RANDOM.nextInt(100);
        int locationX = RANDOM.nextInt(Main.WINDOW_WIDTH);
        int hpMultiplier = 1 << Math.min(10, gameTime / HP_DOUBLE_INTERVAL);

        EnemyFactory factory = selectFactory(
                type,
                bossPresent,
                mobProbability,
                eliteProbability,
                elitePlusProbability
        );
        return factory.createEnemy(
                locationX,
                0,
                0,
                scaleAtLeastOne(speedFor(factory), enemySpeedMultiplier),
                scaleAtLeastOne(hpFor(factory, hpMultiplier), enemyHpMultiplier),
                scaleAtLeastOne(baseBulletPowerFor(factory), enemyBulletPowerMultiplier),
                scaleAtLeastOne(baseBulletSpeedFor(factory), enemyBulletSpeedMultiplier)
        );
    }

    private static EnemyFactory selectFactory(
            int type,
            boolean bossPresent,
            double mobProbability,
            double eliteProbability,
            double elitePlusProbability
    ) {
        int mobThreshold = (int) Math.round(mobProbability * 100);
        int eliteThreshold = mobThreshold + (int) Math.round(eliteProbability * 100);
        int elitePlusThreshold = eliteThreshold + (int) Math.round(elitePlusProbability * 100);
        if (bossPresent) {
            int adjustedMobThreshold = Math.min(90, mobThreshold + 20);
            int adjustedEliteThreshold = Math.min(96, adjustedMobThreshold + Math.max(4, eliteThreshold - mobThreshold));
            int adjustedElitePlusThreshold = Math.min(99, adjustedEliteThreshold + Math.max(3, elitePlusThreshold - eliteThreshold));
            if (type < adjustedMobThreshold) {
                return MOB_FACTORY;
            }
            if (type < adjustedEliteThreshold) {
                return ELITE_FACTORY;
            }
            if (type < adjustedElitePlusThreshold) {
                return ELITE_PLUS_FACTORY;
            }
            return ELITE_PRO_FACTORY;
        }

        if (type < mobThreshold) {
            return MOB_FACTORY;
        }
        if (type < eliteThreshold) {
            return ELITE_FACTORY;
        }
        if (type < elitePlusThreshold) {
            return ELITE_PLUS_FACTORY;
        }
        return ELITE_PRO_FACTORY;
    }

    private static int speedFor(EnemyFactory factory) {
        if (factory instanceof ElitePlusEnemyFactory) {
            return 6;
        }
        if (factory instanceof EliteEnemyFactory || factory instanceof EliteProEnemyFactory) {
            return 5;
        }
        return 4;
    }

    private static int hpFor(EnemyFactory factory, int hpMultiplier) {
        if (factory instanceof EliteEnemyFactory) {
            return 70 * hpMultiplier;
        }
        if (factory instanceof ElitePlusEnemyFactory) {
            return 80 * hpMultiplier;
        }
        if (factory instanceof EliteProEnemyFactory) {
            return 100 * hpMultiplier;
        }
        return 30 * hpMultiplier;
    }

    private static int baseBulletPowerFor(EnemyFactory factory) {
        if (factory instanceof EliteEnemyFactory) {
            return 16;
        }
        if (factory instanceof ElitePlusEnemyFactory) {
            return 18;
        }
        if (factory instanceof EliteProEnemyFactory) {
            return 20;
        }
        return 0;
    }

    private static int baseBulletSpeedFor(EnemyFactory factory) {
        if (factory instanceof EliteEnemyFactory) {
            return ELITE_DIRECT_BULLET_SPEED;
        }
        if (factory instanceof ElitePlusEnemyFactory) {
            return 7;
        }
        if (factory instanceof EliteProEnemyFactory) {
            return 6;
        }
        return 0;
    }

    public static void refreshShootStrategy(
            EnemyAircraft enemyAircraft,
            double enemyBulletPowerMultiplier,
            double enemyBulletSpeedMultiplier
    ) {
        if (enemyAircraft instanceof BossEnemy) {
            enemyAircraft.setShootStrategy(new CircleShootStrategy(
                    20,
                    scaleAtLeastOne(4, enemyBulletSpeedMultiplier),
                    scaleAtLeastOne(22, enemyBulletPowerMultiplier),
                    12
            ));
            return;
        }
        if (enemyAircraft instanceof EliteProEnemy) {
            enemyAircraft.setShootStrategy(new ScatterShootStrategy(
                    new int[]{-2, 0, 2},
                    4,
                    scaleAtLeastOne(6, enemyBulletSpeedMultiplier),
                    scaleAtLeastOne(20, enemyBulletPowerMultiplier)
            ));
            return;
        }
        if (enemyAircraft instanceof ElitePlusEnemy) {
            enemyAircraft.setShootStrategy(new ParallelShootStrategy(
                    new int[]{-12, 12},
                    3,
                    scaleAtLeastOne(7, enemyBulletSpeedMultiplier),
                    scaleAtLeastOne(18, enemyBulletPowerMultiplier)
            ));
            return;
        }
        if (enemyAircraft instanceof EliteEnemy) {
            enemyAircraft.setShootStrategy(new DirectShootStrategy(
                    new int[]{0},
                    2,
                    scaleAtLeastOne(ELITE_DIRECT_BULLET_SPEED, enemyBulletSpeedMultiplier),
                    scaleAtLeastOne(16, enemyBulletPowerMultiplier)
            ));
            return;
        }
        enemyAircraft.setShootStrategy(new NoShootStrategy());
    }

    private static int scaleAtLeastOne(int baseValue, double multiplier) {
        if (baseValue == 0) {
            return 0;
        }
        return Math.max(1, (int) Math.round(baseValue * multiplier));
    }
}
