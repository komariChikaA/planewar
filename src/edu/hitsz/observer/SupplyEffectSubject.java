package edu.hitsz.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class SupplyEffectSubject {

    private final List<SupplyEffectObserver> observers = new ArrayList<>();

    public void addObserver(SupplyEffectObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(SupplyEffectObserver observer) {
        observers.remove(observer);
    }

    public void clearObservers() {
        observers.clear();
    }

    public void notifyBombObservers() {
        for (SupplyEffectObserver observer : new ArrayList<>(observers)) {
            observer.onBombSupply();
        }
    }

    public void notifyFreezeObservers() {
        for (SupplyEffectObserver observer : new ArrayList<>(observers)) {
            observer.onFreezeSupply();
        }
    }
}
