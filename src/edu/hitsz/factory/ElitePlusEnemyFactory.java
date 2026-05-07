package edu.hitsz.factory;

import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.ParallelShootStrategy;

public class ElitePlusEnemyFactory extends EnemyFactory {

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
        EnemyAircraft enemyAircraft = new ElitePlusEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new ParallelShootStrategy(new int[]{-12, 12}, 3, bulletSpeed, bulletPower));
        return enemyAircraft;
    }
}
