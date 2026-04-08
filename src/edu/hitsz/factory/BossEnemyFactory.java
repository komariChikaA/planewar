package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;

public class BossEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        EnemyAircraft enemyAircraft = new BossEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new ScatterShootStrategy(new int[]{-3, -2, -1, -1, 0, 1, 1, 2, 3}, 5, 5, 22));
        return enemyAircraft;
    }
}
