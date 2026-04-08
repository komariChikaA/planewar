package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class FireSupply extends AbstractSupply {

    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft, Game game) {
        heroAircraft.activateFire(1, 1, 10);
        System.out.println("FireSupply active!");
        vanish();
    }
}
