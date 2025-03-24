package controller;

import view.BattleGround;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BattleController extends Thread {
    private final int ANIMATION_TIME = 23;
    private boolean onGoing = true;
    private int firstPlayerScore = 0;
    private int secondPlayerScore = 0;
    private BattleGround battleGround;
    private JLabel firstPlayerLabel;
    private JLabel secondPlayerLabel;

    // These values can be changed in the settings menu
    private int gridSize = 10;
    private boolean hitBoxes = false;
    private int ballSize = 5;
    private int ammoCapacity = 5; //How many bullets can be fired by the player
    private double playerSpeed = 1;

    public BattleController(JLabel firstPlayerLabel, JLabel secondPlayerLabel) {
        this.firstPlayerLabel = firstPlayerLabel;
        this.secondPlayerLabel = secondPlayerLabel;
        battleGround = new BattleGround(500, 500 / gridSize);
    }

    public BattleGround getBattleGround() {
        return battleGround;
    }

    @Override
    public void run() {
        playBackroundMusic();
        while (true) {
            if (onGoing) {
                battleGround.newMatch(500 / gridSize, hitBoxes, ballSize, ammoCapacity, playerSpeed);
            }
            while (onGoing) {
                battleGround.repaint();
                int gameOverScore = battleGround.gameOver();
                if (gameOverScore != 0) {
                    if (gameOverScore == 1) {
                        firstPlayerScore = firstPlayerScore + 1;
                    } else {
                        secondPlayerScore = secondPlayerScore + 1;
                    }
                    onGoing = false;
                    battleGround.gameStop();
                    updateScore();
                }
                try {
                    Thread.sleep(9);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < ANIMATION_TIME; i++) {
                battleGround.repaint();
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            onGoing = true;
        }
    }

    private void updateScore() {
        firstPlayerLabel.setText(String.valueOf(firstPlayerScore));
        secondPlayerLabel.setText(String.valueOf(secondPlayerScore));
    }

    public void applySettings(int gridSize, boolean hitBoxes, int ballSize, int ammoCapacity, double playerSpeed) {
        this.gridSize = gridSize;
        this.hitBoxes = hitBoxes;
        this.ballSize = ballSize;
        this.ammoCapacity = ammoCapacity;
        this.playerSpeed = playerSpeed;
        onGoing = false;
    }

    private void playBackroundMusic() {
        try {
            File musicFile = new File("sound/tank-battle.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);

            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioInputStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

}