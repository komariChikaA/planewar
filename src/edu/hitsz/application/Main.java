package edu.hitsz.application;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setBounds(
                ((int) screenSize.getWidth() - WINDOW_WIDTH) / 2,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameDifficulty difficulty = GameDifficulty.select(frame);
        Game game = Game.create(difficulty);
        frame.add(game);
        frame.setVisible(true);
        game.requestFocusInWindow();
        game.action();
    }
}
