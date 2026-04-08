package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShootStrategy implements ShootStrategy {

    private final int[] speedXs;
    private final int bulletOffsetY;
    private final int bulletSpeedY;
    private final int bulletPower;

    public ScatterShootStrategy(int[] speedXs, int bulletOffsetY, int bulletSpeedY, int bulletPower) {
        this.speedXs = speedXs;
        this.bulletOffsetY = bulletOffsetY;
        this.bulletSpeedY = bulletSpeedY;
        this.bulletPower = bulletPower;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        for (int speedX : speedXs) {
            bullets.add(new EnemyBullet(
                    aircraft.getLocationX() + speedX * 10,
                    aircraft.getLocationY() + bulletOffsetY,
                    speedX,
                    bulletSpeedY,
                    bulletPower
            ));
        }
        return bullets;
    }
}
