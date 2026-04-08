package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class ParallelShootStrategy implements ShootStrategy {

    private final int[] xOffsets;
    private final int bulletOffsetY;
    private final int bulletSpeedY;
    private final int bulletPower;

    public ParallelShootStrategy(int[] xOffsets, int bulletOffsetY, int bulletSpeedY, int bulletPower) {
        this.xOffsets = xOffsets;
        this.bulletOffsetY = bulletOffsetY;
        this.bulletSpeedY = bulletSpeedY;
        this.bulletPower = bulletPower;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        for (int xOffset : xOffsets) {
            bullets.add(new EnemyBullet(
                    aircraft.getLocationX() + xOffset,
                    aircraft.getLocationY() + bulletOffsetY,
                    0,
                    bulletSpeedY,
                    bulletPower
            ));
        }
        return bullets;
    }
}
