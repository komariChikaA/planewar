package edu.hitsz.aircraft;

public class EliteProEnemy extends EnemyAircraft {

    private static final int FREEZE_DURATION = 125;
    private static final double FREEZE_SPEED_SCALE = 0.5;

    public EliteProEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void onBombSupply() {
        decreaseHp(Math.max(1, getMaxHp() / 2));
    }

    @Override
    public void onFreezeSupply() {
        slowFor(FREEZE_DURATION, FREEZE_SPEED_SCALE);
    }
}
