package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class CircleShootStrategy implements ShootStrategy {

    private final int bulletCount;
    private final int bulletSpeed;
    private final int bulletPower;
    private final int spawnRadius;

    public CircleShootStrategy(int bulletCount, int bulletSpeed, int bulletPower, int spawnRadius) {
        this.bulletCount = bulletCount;
        this.bulletSpeed = bulletSpeed;
        this.bulletPower = bulletPower;
        this.spawnRadius = spawnRadius;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        boolean heroAircraft = aircraft instanceof HeroAircraft;
        int power = heroAircraft ? ((HeroAircraft) aircraft).getPower() : bulletPower;
        boolean homing = heroAircraft && ((HeroAircraft) aircraft).isHomingActive();

        for (int i = 0; i < bulletCount; i++) {
            double angle = 2 * Math.PI * i / bulletCount;
            int speedX = (int) Math.round(Math.cos(angle) * bulletSpeed);
            int speedY = (int) Math.round(Math.sin(angle) * bulletSpeed);
            int locationX = aircraft.getLocationX() + (int) Math.round(Math.cos(angle) * spawnRadius);
            int locationY = aircraft.getLocationY() + (int) Math.round(Math.sin(angle) * spawnRadius);

            if (heroAircraft) {
                bullets.add(new HeroBullet(locationX, locationY, speedX, speedY, power, homing));
            } else {
                bullets.add(new EnemyBullet(locationX, locationY, speedX, speedY, power));
            }
        }
        return bullets;
    }
}
