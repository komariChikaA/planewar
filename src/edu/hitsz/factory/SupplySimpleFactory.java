package edu.hitsz.factory;

import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FirePlusSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.FreezeSupply;

import java.util.Random;

public class SupplySimpleFactory {

    private static final int SUPPLY_SPEED_Y = 4;
    private static final Random RANDOM = new Random();

    private SupplySimpleFactory() {
    }

    public static AbstractSupply createRandomSupply(int locationX, int locationY) {
        SupplyType[] supplyTypes = SupplyType.values();
        return createSupply(supplyTypes[RANDOM.nextInt(supplyTypes.length)], locationX, locationY);
    }

    public static AbstractSupply createSupply(SupplyType supplyType, int locationX, int locationY) {
        switch (supplyType) {
            case BLOOD:
                return new BloodSupply(locationX, locationY, 0, SUPPLY_SPEED_Y);
            case FIRE:
                return new FireSupply(locationX, locationY, 0, SUPPLY_SPEED_Y);
            case FIRE_PLUS:
                return new FirePlusSupply(locationX, locationY, 0, SUPPLY_SPEED_Y);
            case BOMB:
                return new BombSupply(locationX, locationY, 0, SUPPLY_SPEED_Y);
            case FREEZE:
            default:
                return new FreezeSupply(locationX, locationY, 0, SUPPLY_SPEED_Y);
        }
    }
}
