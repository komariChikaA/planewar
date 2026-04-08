package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.interface_.Crashable;

import java.util.LinkedList;
import java.util.List;

public class HeroAircraft extends AbstractAircraft implements Crashable {

    private static final int DEFAULT_POWER = 24;
    private static final int POWER_LIMIT = 100;
    private static final int BULLETS_PER_ROW_LIMIT = 6;
    private static final int SHOOT_ROWS_LIMIT = 3;
    private static final int HERO_HITBOX_WIDTH = 6;
    private static final int HERO_HITBOX_HEIGHT = 6;
    private static final int HERO_RENDER_WIDTH = 40;
    private static final int HERO_RENDER_HEIGHT = 40;
    private static final int DODGE_DURATION = 75;
    private static final int DODGE_COOLDOWN = 750;

    private int bulletsPerRow = 1;
    private int shootRows = 1;
    private int power = DEFAULT_POWER;
    private final int direction = -1;
    private int freezeDuration = 0;
    private int homingDuration = 0;
    private int dodgeDuration = 0;
    private int dodgeCooldown = 0;

    private volatile static HeroAircraft instance;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.width = HERO_HITBOX_WIDTH;
        this.height = HERO_HITBOX_HEIGHT;
    }

    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    @Override
    public void forward() {
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int baseX = this.getLocationX();
        int baseY = this.getLocationY() + direction * 2;
        boolean homing = isHomingActive();

        for (int row = 0; row < shootRows; row++) {
            int rowY = baseY + row * 16;
            int rowSpeedY = this.getSpeedY() + direction * (7 + row);
            for (int i = 0; i < bulletsPerRow; i++) {
                BaseBullet bullet = new HeroBullet(
                        baseX + (i * 2 - bulletsPerRow + 1) * 12,
                        rowY,
                        0,
                        rowSpeedY,
                        power,
                        homing
                );
                res.add(bullet);
            }
        }
        return res;
    }

    public void moveBy(int dx, int dy) {
        int nextX = Math.max(0, Math.min(Main.WINDOW_WIDTH, getLocationX() + dx));
        int nextY = Math.max(0, Math.min(Main.WINDOW_HEIGHT, getLocationY() + dy));
        setLocation(nextX, nextY);
    }

    public void activateFire(int bulletAdd, int rowAdd, int powerAdd) {
        bulletsPerRow = Math.min(BULLETS_PER_ROW_LIMIT, bulletsPerRow + bulletAdd);
        shootRows = Math.min(SHOOT_ROWS_LIMIT, shootRows + rowAdd);
        power = Math.min(POWER_LIMIT, power + powerAdd);
    }

    public void activateHoming(int duration) {
        homingDuration = Math.max(homingDuration, duration);
    }

    public boolean isHomingActive() {
        return homingDuration > 0;
    }

    public void reduceHomingDuration(int delta) {
        homingDuration = Math.max(0, homingDuration - delta);
    }

    public void activateFreeze(int duration) {
        freezeDuration = Math.max(freezeDuration, duration);
    }

    public boolean isFreezeActive() {
        return freezeDuration > 0;
    }

    public void reduceFreezeDuration(int delta) {
        freezeDuration = Math.max(0, freezeDuration - delta);
    }

    public boolean activateDodge() {
        if (dodgeCooldown > 0 || dodgeDuration > 0) {
            return false;
        }
        dodgeDuration = DODGE_DURATION;
        dodgeCooldown = DODGE_COOLDOWN;
        return true;
    }

    public boolean isDodging() {
        return dodgeDuration > 0;
    }

    public void reduceDodgeDuration(int delta) {
        dodgeDuration = Math.max(0, dodgeDuration - delta);
    }

    public void reduceDodgeCooldown(int delta) {
        dodgeCooldown = Math.max(0, dodgeCooldown - delta);
    }

    public int getDodgeCooldown() {
        return dodgeCooldown;
    }

    public int getHitboxWidth() {
        return width;
    }

    public int getHitboxHeight() {
        return height;
    }

    public int getRenderWidth() {
        return HERO_RENDER_WIDTH;
    }

    public int getRenderHeight() {
        return HERO_RENDER_HEIGHT;
    }
}
