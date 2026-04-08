package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HeroController {

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shiftPressed;
    private boolean firePressed;
    private boolean dodgeTriggered;

    public HeroController(Game game, HeroAircraft heroAircraft) {
        game.setFocusable(true);
        game.requestFocusInWindow();
        game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        upPressed = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        downPressed = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        leftPressed = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightPressed = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        shiftPressed = true;
                        break;
                    case KeyEvent.VK_Z:
                        firePressed = true;
                        break;
                    case KeyEvent.VK_X:
                        dodgeTriggered = true;
                        break;
                    case KeyEvent.VK_R:
                        game.tryRestart();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        upPressed = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        downPressed = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        leftPressed = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightPressed = false;
                        break;
                    case KeyEvent.VK_SHIFT:
                        shiftPressed = false;
                        break;
                    case KeyEvent.VK_Z:
                        firePressed = false;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void update(HeroAircraft heroAircraft) {
        int speed = shiftPressed ? 12 : 6;
        int moveX = 0;
        int moveY = 0;
        if (leftPressed) {
            moveX -= speed;
        }
        if (rightPressed) {
            moveX += speed;
        }
        if (upPressed) {
            moveY -= speed;
        }
        if (downPressed) {
            moveY += speed;
        }
        heroAircraft.moveBy(moveX, moveY);
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public boolean consumeDodgeTriggered() {
        if (!dodgeTriggered) {
            return false;
        }
        dodgeTriggered = false;
        return true;
    }
}
