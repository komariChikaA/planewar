package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.DirectShootStrategy;

public class EliteEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(
            int locationX,
            int locationY,
            int speedX,
            int speedY,
            int hp,
            int bulletPower,
            int bulletSpeed
    ) {
        EnemyAircraft enemyAircraft = new EliteEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new DirectShootStrategy(new int[]{0}, 2, bulletSpeed, bulletPower));
        return enemyAircraft;
    }
}
