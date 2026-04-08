package edu.hitsz.application;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    private static Clip bgmClip;
    private static Clip bossBgmClip;

    private SoundManager() {
    }

    public static void playBgm() {
        stopBossBgm();
        bgmClip = loop("src/videos/bgm.wav", bgmClip);
    }

    public static void playBossBgm() {
        stopBgm();
        bossBgmClip = loop("src/videos/bgm_boss.wav", bossBgmClip);
    }

    public static void stopBgm() {
        stopClip(bgmClip);
        bgmClip = null;
    }

    public static void stopBossBgm() {
        stopClip(bossBgmClip);
        bossBgmClip = null;
    }

    public static void playBombExplosion() {
        playOnce("src/videos/bomb_explosion.wav");
    }

    public static void playBulletHit() {
        playOnce("src/videos/bullet_hit.wav");
    }

    public static void playSupply() {
        playOnce("src/videos/get_supply.wav");
    }

    public static void playGameOver() {
        stopBgm();
        stopBossBgm();
        playOnce("src/videos/game_over.wav");
    }

    private static Clip loop(String path, Clip oldClip) {
        stopClip(oldClip);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    private static void playOnce(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
        }
    }

    private static void stopClip(Clip clip) {
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.close();
    }
}
