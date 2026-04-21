package edu.hitsz.application;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {

    private static final ExecutorService EFFECT_EXECUTOR = Executors.newFixedThreadPool(3, runnable -> {
        Thread thread = new Thread(runnable, "sound-effect-worker");
        thread.setDaemon(true);
        return thread;
    });

    private static Clip bgmClip;
    private static Clip bossBgmClip;

    private SoundManager() {
    }

    public static synchronized void playBgm() {
        stopBossBgm();
        bgmClip = loop("src/videos/bgm.wav", bgmClip);
    }

    public static synchronized void playBossBgm() {
        stopBgm();
        bossBgmClip = loop("src/videos/bgm_boss.wav", bossBgmClip);
    }

    public static synchronized void stopBgm() {
        stopClip(bgmClip);
        bgmClip = null;
    }

    public static synchronized void stopBossBgm() {
        stopClip(bossBgmClip);
        bossBgmClip = null;
    }

    public static void playBombExplosion() {
        playOnceAsync("src/videos/bomb_explosion.wav");
    }

    public static void playBulletHit() {
        playOnceAsync("src/videos/bullet_hit.wav");
    }

    public static void playSupply() {
        playOnceAsync("src/videos/get_supply.wav");
    }

    public static void playGameOver() {
        stopBgm();
        stopBossBgm();
        playOnceAsync("src/videos/game_over.wav");
    }

    private static Clip loop(String path, Clip oldClip) {
        stopClip(oldClip);
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    private static void playOnceAsync(String path) {
        EFFECT_EXECUTOR.execute(() -> playOnce(path));
    }

    private static void playOnce(String path) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path))) {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> closeOnStop(event, clip));
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
        }
    }

    private static void closeOnStop(LineEvent event, Clip clip) {
        if (event.getType() == LineEvent.Type.STOP) {
            clip.close();
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
