package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.SupplyEffectSubject;

public abstract class AbstractSupply extends AbstractFlyingObject {

    private final SupplyEffectSubject effectSubject = new SupplyEffectSubject() {
    };

    public AbstractSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    public abstract void effect(HeroAircraft heroAircraft, Game game);

    public SupplyEffectSubject getEffectSubject() {
        return effectSubject;
    }
}
