package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class FreezeSupply extends AbstractSupply {

    private static final int FREEZE_DURATION = 180;

    public FreezeSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft, Game game) {
        heroAircraft.activateFreeze(FREEZE_DURATION);
        vanish();
    }
}
