package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.SupplyEffectObserver;
import edu.hitsz.strategy.NoShootStrategy;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
public abstract class EnemyAircraft extends AbstractAircraft implements SupplyEffectObserver {

    private static final double STOP_SPEED_SCALE = 0.0;

    private ShootStrategy shootStrategy = new NoShootStrategy();
    private boolean defeatRewardGranted = false;
    private int effectDuration = 0;
    private boolean permanentStopped = false;
    private boolean movementStopped = false;
    private int originalSpeedX;
    private int originalSpeedY;

    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public boolean grantDefeatReward() {
        if (defeatRewardGranted) {
            return false;
        }
        defeatRewardGranted = true;
        return true;
    }

    @Override
    public void forward() {
        if (permanentStopped) {
            return;
        }
        if (effectDuration > 0) {
            effectDuration--;
            if (movementStopped) {
                if (effectDuration == 0) {
                    restoreSpeed();
                }
                return;
            }
            if (effectDuration == 0) {
                restoreSpeed();
            }
        }
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(this);
    }

    @Override
    public void onBombSupply() {
        decreaseHp(Integer.MAX_VALUE);
    }

    @Override
    public void onFreezeSupply() {
        freezePermanently();
    }

    public boolean canShootNow() {
        return !permanentStopped && !movementStopped;
    }

    protected void freezeFor(int duration) {
        applyTimedSpeedEffect(duration, STOP_SPEED_SCALE);
    }

    protected void slowFor(int duration, double speedScale) {
        applyTimedSpeedEffect(duration, speedScale);
    }

    protected void freezePermanently() {
        if (!permanentStopped) {
            saveOriginalSpeed();
            speedX = 0;
            speedY = 0;
            movementStopped = true;
            permanentStopped = true;
        }
    }

    private void applyTimedSpeedEffect(int duration, double speedScale) {
        if (duration <= effectDuration) {
            return;
        }
        if (effectDuration == 0 && !permanentStopped) {
            saveOriginalSpeed();
        }
        effectDuration = duration;
        movementStopped = speedScale == STOP_SPEED_SCALE;
        speedX = scaledSpeed(originalSpeedX, speedScale);
        speedY = scaledSpeed(originalSpeedY, speedScale);
    }

    private void saveOriginalSpeed() {
        originalSpeedX = speedX;
        originalSpeedY = speedY;
    }

    private int scaledSpeed(int speed, double scale) {
        if (speed == 0 || scale == STOP_SPEED_SCALE) {
            return 0;
        }
        int scaled = (int) Math.round(Math.abs(speed) * scale);
        return (speed > 0 ? 1 : -1) * Math.max(1, scaled);
    }

    private void restoreSpeed() {
        speedX = originalSpeedX;
        speedY = originalSpeedY;
        movementStopped = false;
    }
}
