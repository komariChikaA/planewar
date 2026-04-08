package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.ParallelShootStrategy;

public class EliteEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        EnemyAircraft enemyAircraft = new EliteEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new ParallelShootStrategy(new int[]{-12, 12}, 2, 7, 16));
        return enemyAircraft;
    }
}
