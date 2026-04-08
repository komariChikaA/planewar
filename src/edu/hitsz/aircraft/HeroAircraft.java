package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.interface_.Crashable;
import edu.hitsz.strategy.CircleShootStrategy;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

public class HeroAircraft extends AbstractAircraft implements Crashable {

    private static final int DEFAULT_POWER = 24;
    private static final int POWER_LIMIT = 100;
    private static final int HERO_HITBOX_WIDTH = 6;
    private static final int HERO_HITBOX_HEIGHT = 6;
    private static final int HERO_RENDER_WIDTH = 40;
    private static final int HERO_RENDER_HEIGHT = 40;
    private static final int DODGE_DURATION = 75;
    private static final int DODGE_COOLDOWN = 750;
    private static final int HERO_DIRECT_BULLET_SPEED_Y = -8;
    private static final int HERO_SCATTER_BULLET_SPEED_Y = -7;

    private int power = DEFAULT_POWER;
    private int freezeDuration = 0;
    private int homingDuration = 0;
    private int dodgeDuration = 0;
    private int dodgeCooldown = 0;
    private int scatterShootDuration = 0;
    private int circleShootDuration = 0;

    private final ShootStrategy directShootStrategy = new DirectShootStrategy(new int[]{0}, -2, HERO_DIRECT_BULLET_SPEED_Y, DEFAULT_POWER);
    private final ShootStrategy scatterShootStrategy = new ScatterShootStrategy(new int[]{-2, 0, 2}, -2, HERO_SCATTER_BULLET_SPEED_Y, DEFAULT_POWER);
    private final ShootStrategy circleShootStrategy = new CircleShootStrategy(20, 6, DEFAULT_POWER, 10);
    private ShootStrategy shootStrategy = directShootStrategy;

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
        return shootStrategy.shoot(this);
    }

    public void moveBy(int dx, int dy) {
        int nextX = Math.max(0, Math.min(edu.hitsz.application.Main.WINDOW_WIDTH, getLocationX() + dx));
        int nextY = Math.max(0, Math.min(edu.hitsz.application.Main.WINDOW_HEIGHT, getLocationY() + dy));
        setLocation(nextX, nextY);
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

    public void activateScatterShoot(int duration) {
        scatterShootDuration = Math.max(scatterShootDuration, duration);
        refreshShootStrategy();
    }

    public void activateCircleShoot(int duration) {
        circleShootDuration = Math.max(circleShootDuration, duration);
        refreshShootStrategy();
    }

    public void reduceScatterShootDuration(int delta) {
        scatterShootDuration = Math.max(0, scatterShootDuration - delta);
        refreshShootStrategy();
    }

    public void reduceCircleShootDuration(int delta) {
        circleShootDuration = Math.max(0, circleShootDuration - delta);
        refreshShootStrategy();
    }

    public boolean isScatterShootActive() {
        return scatterShootDuration > 0;
    }

    public boolean isCircleShootActive() {
        return circleShootDuration > 0;
    }

    public String getShootModeName() {
        if (isCircleShootActive()) {
            return "RING";
        }
        if (isScatterShootActive()) {
            return "SCATTER";
        }
        return "DIRECT";
    }

    public int getPower() {
        return power;
    }

    public void increasePower(int powerAdd) {
        power = Math.min(POWER_LIMIT, power + powerAdd);
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

    private void refreshShootStrategy() {
        if (circleShootDuration > 0) {
            shootStrategy = circleShootStrategy;
            return;
        }
        if (scatterShootDuration > 0) {
            shootStrategy = scatterShootStrategy;
            return;
        }
        shootStrategy = directShootStrategy;
    }
}
