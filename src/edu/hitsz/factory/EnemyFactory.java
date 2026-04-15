package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;

public abstract class EnemyFactory {

    public abstract EnemyAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int bulletPower);
}
