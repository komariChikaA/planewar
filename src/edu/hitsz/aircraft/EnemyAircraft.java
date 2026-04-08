package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.NoShootStrategy;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
public abstract class EnemyAircraft extends AbstractAircraft{

    private ShootStrategy shootStrategy = new NoShootStrategy();
    private boolean defeatRewardGranted = false;

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
}
