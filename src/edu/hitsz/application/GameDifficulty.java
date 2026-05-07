package edu.hitsz.application;

import javax.swing.JOptionPane;
import java.awt.Component;

public enum GameDifficulty {

    EASY("\u7b80\u5355\u6a21\u5f0f", false, 0.80, 0.75, 0.70, 100),
    NORMAL("\u666e\u901a\u6a21\u5f0f", false, 1.00, 1.00, 1.00, 100),
    HARD("\u56f0\u96be\u6a21\u5f0f", true, 1.20, 1.15, 1.20, 100);

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

    public double getEnemyHpMultiplier() {
        return enemyHpMultiplier;
    }

    public double getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }

    public double getEnemyBulletPowerMultiplier() {
        return enemyBulletPowerMultiplier;
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
                        "\u8bf7\u9009\u62e9\u6e38\u620f\u96be\u5ea6\uff1a",
                        "\u7b80\u5355\u6a21\u5f0f\uff1a\u654c\u673a\u8f83\u5c11\u4e14\u8f83\u5f31\uff0c\u4e0d\u751f\u6210 Boss\uff0c\u96be\u5ea6\u4e0d\u968f\u65f6\u95f4\u63d0\u5347",
                        "\u666e\u901a\u6a21\u5f0f\uff1a\u53ef\u751f\u6210 Boss\uff0c\u654c\u673a\u751f\u6210\u5468\u671f\u3001\u901f\u5ea6\u3001\u8840\u91cf\u548c\u5b50\u5f39\u901f\u5ea6\u4f1a\u9010\u6b65\u63d0\u5347",
                        "\u56f0\u96be\u6a21\u5f0f\uff1aBoss \u66f4\u5f3a\uff0c\u96be\u5ea6\u63d0\u5347\u66f4\u5feb\uff0c\u654c\u673a\u5c04\u51fb\u9891\u7387\u4e5f\u4f1a\u63d0\u5347"
                ),
                "Aircraft War Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                NORMAL
        );
        return selected == null ? NORMAL : selected;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
