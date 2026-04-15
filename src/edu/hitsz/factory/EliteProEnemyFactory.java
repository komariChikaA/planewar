package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;

public class EliteProEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int bulletPower) {
        EnemyAircraft enemyAircraft = new EliteProEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new ScatterShootStrategy(new int[]{-2, 0, 2}, 4, 6, bulletPower));
        return enemyAircraft;
    }
}
