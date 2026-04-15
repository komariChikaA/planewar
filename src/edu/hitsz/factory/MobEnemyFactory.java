package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.strategy.NoShootStrategy;

public class MobEnemyFactory extends EnemyFactory {

    @Override
    public EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int bulletPower) {
        EnemyAircraft enemyAircraft = new MobEnemy(locationX, locationY, speedX, speedY, hp);
        enemyAircraft.setShootStrategy(new NoShootStrategy());
        return enemyAircraft;
    }
}
