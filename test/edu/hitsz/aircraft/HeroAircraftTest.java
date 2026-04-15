package edu.hitsz.aircraft;

import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.FirePlusSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.FreezeSupply;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @Before
    public void setUp() {
        HeroAircraft.resetInstance();
        heroAircraft = HeroAircraft.getInstance(200, 300, 0, 0, 100);
    }

    @After
    public void tearDown() {
        HeroAircraft.resetInstance();
    }

    @Test
    public void shouldNotCrashWhenBulletTouchesHeroBodyButMissesHitPoint() {
        EnemyBullet bullet = new EnemyBullet(
                heroAircraft.getLocationX() + heroAircraft.getRenderWidth() / 2 - 5,
                heroAircraft.getLocationY(),
                0,
                0,
                10
        );

        assertFalse(heroAircraft.crash(bullet));
        assertFalse(bullet.crash(heroAircraft));
    }

    @Test
    public void shouldCrashWhenBulletHitsHeroJudgementPoint() {
        EnemyBullet bullet = new EnemyBullet(
                heroAircraft.getLocationX(),
                heroAircraft.getLocationY(),
                0,
                0,
                10
        );

        assertTrue(heroAircraft.crash(bullet));
        assertTrue(bullet.crash(heroAircraft));
    }

    @Test
    public void shouldCrashWhenSupplyTouchesHeroBody() {
        BloodSupply bloodSupply = new BloodSupply(
                heroAircraft.getLocationX() + heroAircraft.getRenderWidth() / 2 - 5,
                heroAircraft.getLocationY(),
                0,
                0
        );

        assertTrue(heroAircraft.crash(bloodSupply) || bloodSupply.crash(heroAircraft));
    }

    @Test
    public void shouldIncreaseHpWhenCollectingBloodSupply() {
        heroAircraft.decreaseHp(20);
        BloodSupply bloodSupply = new BloodSupply(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 0);

        bloodSupply.effect(heroAircraft, null);

        assertEquals(100, heroAircraft.getHp());
        assertTrue(bloodSupply.notValid());
    }

    @Test
    public void shouldActivateFreezeWhenCollectingFreezeSupply() {
        FreezeSupply freezeSupply = new FreezeSupply(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 0);

        freezeSupply.effect(heroAircraft, null);

        assertTrue(heroAircraft.isFreezeActive());
        assertTrue(freezeSupply.notValid());
    }

    @Test
    public void shouldActivateScatterShootWhenCollectingFireSupply() {
        FireSupply fireSupply = new FireSupply(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 0);

        fireSupply.effect(heroAircraft, null);

        assertTrue(heroAircraft.isScatterShootActive());
        assertEquals("SCATTER", heroAircraft.getShootModeName());
        assertTrue(fireSupply.notValid());
    }

    @Test
    public void shouldActivateCircleShootWhenCollectingFirePlusSupply() {
        FirePlusSupply firePlusSupply = new FirePlusSupply(heroAircraft.getLocationX(), heroAircraft.getLocationY(), 0, 0);

        firePlusSupply.effect(heroAircraft, null);

        assertTrue(heroAircraft.isCircleShootActive());
        assertEquals("RING", heroAircraft.getShootModeName());
        assertTrue(firePlusSupply.notValid());
    }
}
