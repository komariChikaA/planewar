package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.factory.EnemySimpleFactory;
import edu.hitsz.factory.SupplySimpleFactory;
import edu.hitsz.factory.SupplyType;
import edu.hitsz.supply.AbstractSupply;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JPanel {

    private static final int FREEZE_MOVE_DIVISOR = 3;
    private static final int BOSS_TRIGGER_SCORE = 3000;
    private static final SupplyType[] ELITE_SUPPLIES = {
            SupplyType.BLOOD, SupplyType.FIRE, SupplyType.FIRE_PLUS, SupplyType.BOMB
    };
    private static final SupplyType[] PRO_SUPPLIES = {
            SupplyType.BLOOD, SupplyType.FIRE, SupplyType.FIRE_PLUS, SupplyType.BOMB, SupplyType.FREEZE
    };
    private final Random random = new Random();
    private int backGroundTop = 0;
    private int time = 0;
    private final Timer timer;
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final HeroController heroController;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractSupply> supplies;

    private final int enemyMaxNumber = 5;
    protected double enemySpawnCycle = 20;
    private int enemySpawnCounter = 0;
    protected double shootCycle = 6;
    private int shootCounter = 0;
    private int score = 0;
    private boolean gameOverFlag = false;
    private boolean gameClearFlag = false;
    private boolean bossSpawned = false;
    private int heroTrailX;
    private int heroTrailY;

    public Game() {
        heroAircraft = HeroAircraft.getInstance(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0,
                0,
                100
        );

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supplies = new LinkedList<>();

        heroController = new HeroController(this, heroAircraft);
        heroTrailX = heroAircraft.getLocationX();
        heroTrailY = heroAircraft.getLocationY();
        this.timer = new Timer("game-action-timer", true);
        SoundManager.playBgm();
    }

    public void action() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (gameOverFlag || gameClearFlag) {
                    return;
                }
                time++;
                updateHeroStatus();
                updateHeroControl();
                spawnEnemyAction();
                shootAction();
                bulletsMoveAction();
                aircraftsMoveAction();
                crashCheckAction();
                postProcessAction();
                repaint();
                checkResultAction();
            }
        };
        timer.schedule(task, 0, timeInterval);
    }

    private void updateHeroStatus() {
        if (heroAircraft.isFreezeActive()) {
            heroAircraft.reduceFreezeDuration(1);
        }
        if (heroAircraft.isHomingActive()) {
            heroAircraft.reduceHomingDuration(1);
        }
        if (heroAircraft.isDodging()) {
            heroAircraft.reduceDodgeDuration(1);
        }
        if (heroAircraft.getDodgeCooldown() > 0) {
            heroAircraft.reduceDodgeCooldown(1);
        }
    }

    private void updateHeroControl() {
        heroTrailX += (heroAircraft.getLocationX() - heroTrailX) / 3;
        heroTrailY += (heroAircraft.getLocationY() - heroTrailY) / 3;
        heroController.update(heroAircraft);
        if (heroController.consumeDodgeTriggered()) {
            heroAircraft.activateDodge();
        }
    }

    private void spawnEnemyAction() {
        enemySpawnCounter++;
        if (enemySpawnCounter < enemySpawnCycle) {
            return;
        }
        enemySpawnCounter = 0;

        if (!bossSpawned && score >= BOSS_TRIGGER_SCORE) {
            bossSpawned = true;
            enemyAircrafts.add(EnemySimpleFactory.createBoss());
            SoundManager.playBossBgm();
            return;
        }

        if (enemyAircrafts.size() < enemyMaxNumber) {
            enemyAircrafts.add(EnemySimpleFactory.createEnemy(time, hasActiveBoss()));
        }
    }

    private boolean hasActiveBoss() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof BossEnemy && !enemyAircraft.notValid()) {
                return true;
            }
        }
        return false;
    }

    private void shootAction() {
        if (!heroController.isFirePressed()) {
            shootCounter = 0;
        } else {
            shootCounter++;
            if (shootCounter >= shootCycle) {
                shootCounter = 0;
                heroBullets.addAll(heroAircraft.shoot());
            }
        }

        if (heroAircraft.isFreezeActive()) {
            return;
        }
        if (time % (int) shootCycle != 0) {
            return;
        }
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (canEnemyShoot(enemyAircraft)) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            if (bullet instanceof HeroBullet) {
                HeroBullet heroBullet = (HeroBullet) bullet;
                if (heroBullet.isHoming()) {
                    heroBullet.trackTarget(findNearestEnemy(heroBullet));
                }
            }
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private AbstractAircraft findNearestEnemy(BaseBullet bullet) {
        AbstractAircraft nearest = null;
        int bestDistance = Integer.MAX_VALUE;
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            int dx = enemyAircraft.getLocationX() - bullet.getLocationX();
            int dy = enemyAircraft.getLocationY() - bullet.getLocationY();
            int distance = dx * dx + dy * dy;
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = enemyAircraft;
            }
        }
        return nearest;
    }

    private void aircraftsMoveAction() {
        boolean freezeSkipsThisFrame = heroAircraft.isFreezeActive() && time % FREEZE_MOVE_DIVISOR != 0;
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (!freezeSkipsThisFrame) {
                enemyAircraft.forward();
            }
        }
        for (AbstractSupply supply : supplies) {
            supply.forward();
        }
    }

    private void crashCheckAction() {
        if (!heroAircraft.isDodging()) {
            for (BaseBullet bullet : enemyBullets) {
                if (bullet.notValid()) {
                    continue;
                }
                if (heroAircraft.crash(bullet) || bullet.crash(heroAircraft)) {
                    heroAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                }
            }
        }

        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        EnemyAircraft defeatedEnemy = (EnemyAircraft) enemyAircraft;
                        if (defeatedEnemy.grantDefeatReward()) {
                            SoundManager.playBulletHit();
                            score += scoreForEnemy(enemyAircraft);
                            maybeGenerateSupply(enemyAircraft);
                            if (enemyAircraft instanceof BossEnemy) {
                                triggerGameClear();
                                return;
                            }
                        }
                    }
                    break;
                }
                if (!heroAircraft.isDodging()
                        && (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft))) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        for (AbstractSupply supply : supplies) {
            if (supply.notValid()) {
                continue;
            }
            if (heroAircraft.crash(supply) || supply.crash(heroAircraft)) {
                SoundManager.playSupply();
                supply.effect(heroAircraft, this);
            }
        }
    }

    private int scoreForEnemy(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof BossEnemy) {
            return 3000;
        }
        if (enemyAircraft instanceof EliteProEnemy) {
            return 35;
        }
        if (enemyAircraft instanceof ElitePlusEnemy) {
            return 25;
        }
        if (enemyAircraft instanceof EliteEnemy) {
            return 20;
        }
        return 10;
    }

    private void maybeGenerateSupply(AbstractAircraft enemyAircraft) {
        SupplyType[] supplyPool = supplyPoolFor(enemyAircraft);
        if (supplyPool.length > 0) {
            dropRandomSupply(enemyAircraft.getLocationX(), enemyAircraft.getLocationY(), supplyPool);
        }
    }

    private void dropRandomSupply(int locationX, int locationY, SupplyType... supplyTypes) {
        SupplyType supplyType = supplyTypes[random.nextInt(supplyTypes.length)];
        supplies.add(SupplySimpleFactory.createSupply(supplyType, locationX, locationY));
    }

    private boolean canEnemyShoot(AbstractAircraft enemyAircraft) {
        return enemyAircraft instanceof EliteEnemy
                || enemyAircraft instanceof ElitePlusEnemy
                || enemyAircraft instanceof EliteProEnemy
                || enemyAircraft instanceof BossEnemy;
    }

    private SupplyType[] supplyPoolFor(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof EliteEnemy) {
            return ELITE_SUPPLIES;
        }
        if (enemyAircraft instanceof EliteProEnemy || enemyAircraft instanceof BossEnemy) {
            return PRO_SUPPLIES;
        }
        return new SupplyType[0];
    }

    public void activateBomb() {
        boolean clearedBoss = false;
        SoundManager.playBombExplosion();
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (!enemyAircraft.notValid() && !(enemyAircraft instanceof BossEnemy)) {
                enemyAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.vanish();
        }
        score += 50;
    }

    private void triggerGameClear() {
        gameClearFlag = true;
        timer.cancel();
        SoundManager.stopBossBgm();
        SoundManager.stopBgm();
        repaint();
        System.out.println("Victory!");
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();
            gameOverFlag = true;
            SoundManager.playGameOver();
            System.out.println("Game Over!");
        }
    }

    public void tryRestart() {
        if (!gameOverFlag) {
            return;
        }
        SoundManager.stopBgm();
        SoundManager.stopBossBgm();
        HeroAircraft.resetInstance();
        Game newGame = new Game();
        Container container = SwingUtilities.getWindowAncestor(this);
        if (container != null) {
            container.remove(this);
            container.add(newGame);
            container.validate();
            container.repaint();
            newGame.requestFocusInWindow();
            newGame.action();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g2d.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        paintImageWithPositionRevised(g2d, enemyAircrafts);
        paintImageWithPositionRevised(g2d, supplies);

        if (heroController.isShiftPressed()) {
            paintHeroBoostEffect(g2d);
        }

        g2d.drawImage(
                ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - heroAircraft.getRenderWidth() / 2,
                heroAircraft.getLocationY() - heroAircraft.getRenderHeight() / 2,
                heroAircraft.getRenderWidth(),
                heroAircraft.getRenderHeight(),
                null
        );
        paintImageWithPositionRevised(g2d, enemyBullets);
        paintImageWithPositionRevised(g2d, heroBullets);

        g2d.setColor(heroAircraft.isDodging() ? Color.CYAN : Color.RED);
        g2d.fillOval(
                heroAircraft.getLocationX() - heroAircraft.getHitboxWidth() / 2,
                heroAircraft.getLocationY() - heroAircraft.getHitboxHeight() / 2,
                heroAircraft.getHitboxWidth(),
                heroAircraft.getHitboxHeight()
        );

        paintScoreAndLife(g2d);
        g2d.dispose();
    }

    private void paintHeroBoostEffect(Graphics2D g2d) {
        int centerX = heroAircraft.getLocationX();
        int centerY = heroAircraft.getLocationY();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2d.setColor(new Color(120, 255, 255));
        g2d.fillOval(centerX - 34, centerY - 34, 68, 68);

        int dx = centerX - heroTrailX;
        int dy = centerY - heroTrailY;
        for (int i = 1; i <= 3; i++) {
            float alpha = 0.16f - i * 0.03f;
            if (alpha <= 0) {
                continue;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int trailX = centerX - dx * i / 2;
            int trailY = centerY - dy * i / 2;
            int width = heroAircraft.getRenderWidth() - i * 4;
            int height = heroAircraft.getRenderHeight() - i * 4;
            g2d.drawImage(
                    ImageManager.HERO_IMAGE,
                    trailX - width / 2,
                    trailY - height / 2,
                    width,
                    height,
                    null
            );
        }
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(new Color(180, 255, 255, 180));
        g2d.drawOval(centerX - 24, centerY - 24, 48, 48);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image!";
            g.drawImage(
                    image,
                    object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2,
                    null
            );
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y += 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
        if (heroAircraft.isFreezeActive()) {
            y += 20;
            g.drawString("FREEZE", x, y);
        }
        if (heroAircraft.isHomingActive()) {
            y += 20;
            g.drawString("HOMING", x, y);
        }
        if (heroAircraft.isDodging()) {
            y += 20;
            g.drawString("DODGE", x, y);
        } else if (heroAircraft.getDodgeCooldown() > 0) {
            y += 20;
            g.drawString("DODGE CD: " + heroAircraft.getDodgeCooldown() / 25, x, y);
        }
        if (gameClearFlag) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            g.drawString("YOU WIN", Main.WINDOW_WIDTH / 2 - 90, Main.WINDOW_HEIGHT / 2);
        }
    }
}
