package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
import java.util.List;

public abstract class AbstractAircraft extends AbstractFlyingObject {

    // 最大生命值
    protected int maxHp;

    // 当前生命值
    protected int hp;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish();  // 失效（被击毁）
        }
    }


    public void increaseHp(int add) {
        hp += add;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }


    public int getHp() {
        return hp;
    }


    public int getMaxHp() {
        return maxHp;
    }


    //是否存活
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * 飞机射击方法（抽象）
     * 子类必须实现
     */
    public abstract List<BaseBullet> shoot();
}


