package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class BloodSupply extends AbstractSupply {

    private static final int HEAL_AMOUNT = 40;

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft, Game game) {
        heroAircraft.increaseHp(HEAL_AMOUNT);
        vanish();
    }
}
