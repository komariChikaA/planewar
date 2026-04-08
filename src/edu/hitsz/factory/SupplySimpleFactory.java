package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FirePlusSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.FreezeSupply;

import java.util.Random;

public class SupplySimpleFactory {

    private static final int SUPPLY_SPEED_Y = 4;
    private static final SupplyType[] ELITE_PLUS_SUPPLIES = {
            SupplyType.BLOOD, SupplyType.FIRE, SupplyType.FIRE_PLUS, SupplyType.BOMB
    };
    private static final SupplyType[] ELITE_PRO_SUPPLIES = {
            SupplyType.BLOOD, SupplyType.FIRE, SupplyType.FIRE_PLUS, SupplyType.BOMB, SupplyType.FREEZE
    };
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

    public static AbstractSupply createSupplyForEnemy(AbstractAircraft enemyAircraft) {
        SupplyType[] supplyPool = supplyPoolFor(enemyAircraft);
        if (supplyPool.length == 0) {
            return null;
        }
        SupplyType supplyType = supplyPool[RANDOM.nextInt(supplyPool.length)];
        return createSupply(supplyType, enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
    }

    private static SupplyType[] supplyPoolFor(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof ElitePlusEnemy) {
            return ELITE_PLUS_SUPPLIES;
        }
        if (enemyAircraft instanceof EliteProEnemy) {
            return ELITE_PRO_SUPPLIES;
        }
        return new SupplyType[0];
    }
}
