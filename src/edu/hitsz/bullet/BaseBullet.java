package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.SupplyEffectObserver;

/**
 * 子弹基类
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject implements SupplyEffectObserver {

    private static final int FREEZE_DURATION = 125;

    private int power = 0;
    private int freezeDuration = 0;
    private int originalSpeedX;
    private int originalSpeedY;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        if (freezeDuration > 0) {
            freezeDuration--;
            if (freezeDuration == 0) {
                speedX = originalSpeedX;
                speedY = originalSpeedY;
            }
            return;
        }
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    @Override
    public void onBombSupply() {
        vanish();
    }

    @Override
    public void onFreezeSupply() {
        if (freezeDuration == 0) {
            originalSpeedX = speedX;
            originalSpeedY = speedY;
            speedX = 0;
            speedY = 0;
        }
        freezeDuration = Math.max(freezeDuration, FREEZE_DURATION);
    }
}
