package edu.hitsz.application;

import javax.swing.JOptionPane;
import java.awt.Component;

public enum GameDifficulty {

    EASY("EASY", false, 0.60, 0.75, 0.60, 100),
    ADVANCED("ADVANCED", false, 0.85, 0.85, 0.75, 100),
    EXPERT("EXPERT", true, 1.00, 1.00, 1.00, 100),
    MASTER("MASTER", true, 3.00, 1.00, 1.00, 100),
    RE_MASTER("RE:MASTER", true, 3.00, 1.00, 1.00, 20);

    private final String displayName;
    private final boolean bossMovable;
    private final double enemyHpMultiplier;
    private final double enemySpeedMultiplier;
    private final double enemyBulletPowerMultiplier;
    private final int heroInitialHp;

    GameDifficulty(
            String displayName,
            boolean bossMovable,
            double enemyHpMultiplier,
            double enemySpeedMultiplier,
            double enemyBulletPowerMultiplier,
            int heroInitialHp
    ) {
        this.displayName = displayName;
        this.bossMovable = bossMovable;
        this.enemyHpMultiplier = enemyHpMultiplier;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.enemyBulletPowerMultiplier = enemyBulletPowerMultiplier;
        this.heroInitialHp = heroInitialHp;
    }

    public boolean isBossMovable() {
        return bossMovable;
    }

    public int scaleEnemyHp(int baseHp) {
        return Math.max(1, (int) Math.round(baseHp * enemyHpMultiplier));
    }

    public int scaleEnemySpeed(int baseSpeed) {
        return Math.max(1, (int) Math.round(baseSpeed * enemySpeedMultiplier));
    }

    public int scaleEnemyBulletPower(int baseBulletPower) {
        return Math.max(1, (int) Math.round(baseBulletPower * enemyBulletPowerMultiplier));
    }

    public int getHeroInitialHp() {
        return heroInitialHp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GameDifficulty select(Component parent) {
        GameDifficulty[] options = values();
        GameDifficulty selected = (GameDifficulty) JOptionPane.showInputDialog(
                parent,
                String.join(
                        "\n",
                        "Select difficulty:",
                        "EASY: static boss, lower enemy HP, slower enemies, weaker bullets",
                        "ADVANCED: static boss, slightly tougher enemies and bullets",
                        "EXPERT: moving boss, standard enemy stats",
                        "MASTER: moving boss, enemy HP is tripled",
                        "RE:MASTER: MASTER rules, but hero HP is capped at 20"
                ),
                "Aircraft War Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                EXPERT
        );
        return selected == null ? EXPERT : selected;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
