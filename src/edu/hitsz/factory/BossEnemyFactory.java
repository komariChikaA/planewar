package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.CircleShootStrategy;

public class BossEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int bulletPower) {
        EnemyAircraft enemyAircraft = new BossEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new CircleShootStrategy(20, 4, bulletPower, 12));
        return enemyAircraft;
    }
}
