package edu.hitsz.bullet;

import edu.hitsz.aircraft.AbstractAircraft;

public class HeroBullet extends BaseBullet {

    private final boolean homing;

    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power, boolean homing) {
        super(locationX, locationY, speedX, speedY, power);
        this.homing = homing;
    }

    public boolean isHoming() {
        return homing;
    }

    public void trackTarget(AbstractAircraft target) {
        if (!homing || target == null || target.notValid()) {
            return;
        }

        int dx = target.getLocationX() - this.getLocationX();
        int dy = target.getLocationY() - this.getLocationY();

        if (dx > 0) {
            speedX = Math.min(speedX + 1, 6);
        } else if (dx < 0) {
            speedX = Math.max(speedX - 1, -6);
        }

        if (dy < 0) {
            speedY = Math.max(speedY - 1, -12);
        } else if (dy > 0) {
            speedY = Math.min(speedY + 1, 3);
        }
    }
}
