package edu.hitsz.aircraft;

public class ElitePlusEnemy extends EnemyAircraft {

    private static final int FREEZE_DURATION = 75;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void onFreezeSupply() {
        freezeFor(FREEZE_DURATION);
    }
}
