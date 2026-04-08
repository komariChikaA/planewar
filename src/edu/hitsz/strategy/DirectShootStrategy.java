package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class DirectShootStrategy implements ShootStrategy {

    private final int[] xOffsets;
    private final int bulletOffsetY;
    private final int bulletSpeedY;
    private final int bulletPower;

    public DirectShootStrategy(int[] xOffsets, int bulletOffsetY, int bulletSpeedY, int bulletPower) {
        this.xOffsets = xOffsets;
        this.bulletOffsetY = bulletOffsetY;
        this.bulletSpeedY = bulletSpeedY;
        this.bulletPower = bulletPower;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        boolean heroAircraft = aircraft instanceof HeroAircraft;
        int power = heroAircraft ? ((HeroAircraft) aircraft).getPower() : bulletPower;
        boolean homing = heroAircraft && ((HeroAircraft) aircraft).isHomingActive();

        for (int xOffset : xOffsets) {
            if (heroAircraft) {
                bullets.add(new HeroBullet(
                        aircraft.getLocationX() + xOffset,
                        aircraft.getLocationY() + bulletOffsetY,
                        0,
                        bulletSpeedY,
                        power,
                        homing
                ));
            } else {
                bullets.add(new EnemyBullet(
                        aircraft.getLocationX() + xOffset,
                        aircraft.getLocationY() + bulletOffsetY,
                        0,
                        bulletSpeedY,
                        power
                ));
            }
        }
        return bullets;
    }
}
