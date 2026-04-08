package edu.hitsz.factory;

import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;

public class ElitePlusEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        EnemyAircraft enemyAircraft = new ElitePlusEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new ScatterShootStrategy(new int[]{-2, -1, -1, 0, 1, 1, 2}, 3, 9, 18));
        return enemyAircraft;
    }
}
