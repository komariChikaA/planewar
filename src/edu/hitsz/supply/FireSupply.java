package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class FireSupply extends AbstractSupply {

    private static final int SHOOT_MODE_DURATION = 500;

    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft, Game game) {
        heroAircraft.activateScatterShoot(SHOOT_MODE_DURATION);
        System.out.println("FireSupply active!");
        vanish();
    }
}
