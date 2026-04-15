package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.GameDifficulty;
import edu.hitsz.application.Main;

import java.util.Random;

public class EnemySimpleFactory {

    private static final Random RANDOM = new Random();
    private static final int HP_DOUBLE_INTERVAL = 1500;
    private static final int BOSS_HP = 20000;

    private static final EnemyFactory MOB_FACTORY = new MobEnemyFactory();
    private static final EnemyFactory ELITE_FACTORY = new EliteEnemyFactory();
    private static final EnemyFactory ELITE_PLUS_FACTORY = new ElitePlusEnemyFactory();
    private static final EnemyFactory ELITE_PRO_FACTORY = new EliteProEnemyFactory();
    private static final EnemyFactory BOSS_FACTORY = new BossEnemyFactory();

    private EnemySimpleFactory() {
    }

    public static EnemyAircraft createBoss(GameDifficulty difficulty) {
        int speedX = difficulty.isBossMovable() ? 3 : 0;
        return BOSS_FACTORY.createEnemy(
                Main.WINDOW_WIDTH / 2,
                0,
                speedX,
                0,
                difficulty.scaleEnemyHp(BOSS_HP),
                difficulty.scaleEnemyBulletPower(22)
        );
    }

    public static EnemyAircraft createEnemy(int gameTime, boolean bossPresent, GameDifficulty difficulty) {
        int type = RANDOM.nextInt(100);
        int locationX = RANDOM.nextInt(Main.WINDOW_WIDTH);
        int hpMultiplier = 1 << Math.min(10, gameTime / HP_DOUBLE_INTERVAL);

        EnemyFactory factory = selectFactory(type, bossPresent);
        return factory.createEnemy(
                locationX,
                0,
                0,
                difficulty.scaleEnemySpeed(speedFor(factory)),
                difficulty.scaleEnemyHp(hpFor(factory, hpMultiplier)),
                bulletPowerFor(factory, difficulty)
        );
    }

    private static EnemyFactory selectFactory(int type, boolean bossPresent) {
        if (bossPresent) {
            if (type < 78) {
                return MOB_FACTORY;
            }
            if (type < 86) {
                return ELITE_FACTORY;
            }
            if (type < 93) {
                return ELITE_PLUS_FACTORY;
            }
            return ELITE_PRO_FACTORY;
        }

        if (type < 50) {
            return MOB_FACTORY;
        }
        if (type < 70) {
            return ELITE_FACTORY;
        }
        if (type < 85) {
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

    private static int bulletPowerFor(EnemyFactory factory, GameDifficulty difficulty) {
        if (factory instanceof EliteEnemyFactory) {
            return difficulty.scaleEnemyBulletPower(16);
        }
        if (factory instanceof ElitePlusEnemyFactory) {
            return difficulty.scaleEnemyBulletPower(18);
        }
        if (factory instanceof EliteProEnemyFactory) {
            return difficulty.scaleEnemyBulletPower(20);
        }
        return 0;
    }
}
