package view;

import controller.BallManager;
import controller.PlayerController;
import modell.Ball;
import tools.MapGenerator;
import modell.Player;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class BattleGround extends JPanel {
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    private Set<Rectangle> lines;
    private Player firstPlayer;
    private Player secondPlayer;
    private PlayerController firstPlayerController;
    private PlayerController secondPlayerController;
    private BallManager ballManager;
    private Random random;
    private int panelSize;
    private int tileSize;
    private boolean hitBoxes = false;
    private int ballSize = 5;
    private double playerSpeed = 1;

    public BattleGround(int panelSize, int tileSize) {
        random = new Random();
        this.panelSize = panelSize;
        this.tileSize = tileSize;
        setPreferredSize(new Dimension(panelSize, panelSize));
        setBackground(Color.white);
    }

    // Loads a new match with the given settings
    public void newMatch(int tileSize, boolean hitBoxes, int ballSize, int ammoCapacity, double playerSpeed) {
        this.tileSize = tileSize;
        this.hitBoxes = hitBoxes;
        this.ballSize = ballSize;
        this.playerSpeed = playerSpeed;
        lines = MapGenerator.generateLines(panelSize, tileSize);
        randomPlayerSpawn();
        ballManager = new BallManager(ammoCapacity);
        ballManager.start();
    }

    // Spawns two players on the map
    public void randomPlayerSpawn() {
        // Generate random player position
        int n = panelSize / tileSize;
        int randX = random.nextInt(n);
        int randY = random.nextInt(n);
        int randAngle = random.nextInt(360);

        firstPlayer = new Player(playerSpeed, hitBoxes, randX * tileSize + 10, randY * tileSize + 15, randAngle, 1);
        firstPlayerController = new PlayerController(firstPlayer, this);

        // Ensure that the two players don't get placed on the same spot
        int prevX = randX;
        int prevY = randY;
        do {
            randX = random.nextInt(n);
            randY = random.nextInt(n);
        } while (prevX == randX && prevY == randY);

        randAngle = random.nextInt(360);
        secondPlayer = new Player(playerSpeed, hitBoxes, randX * tileSize + 10, randY * tileSize + 15, randAngle, 2);
        secondPlayerController = new PlayerController(secondPlayer, this);

        firstPlayerController.start();
        secondPlayerController.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draw grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (Rectangle line : lines) {
            g.fillRect(line.x, line.y, line.width, line.height);
        }

        ballManager.paintBalls(g);
        firstPlayer.drawPlayer(g);
        secondPlayer.drawPlayer(g);
    }

    // Returns a number indicating the collision
    // 0 - no collision occurred
    // 1 - collision occurred with a vertical wall
    // 2 - collision occurred with a horizontal wall
    public int isCollidingWithGrid(Player player) {
        HashMap<String, Line2D> playerHitBox = player.getPlayerHitBox();

        Line2D top = playerHitBox.get("top");
        Line2D left = playerHitBox.get("left");
        Line2D bottom = playerHitBox.get("bottom");
        Line2D right = playerHitBox.get("right");

        //System.out.println(x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + " " + x4 + " " + y4);

        for (Rectangle line : lines) {
            if (line.intersectsLine(top) || line.intersectsLine(left) || line.intersectsLine(bottom) || line.intersectsLine(right)) {
                // If the line is a vertical one
                if (line.getWidth() < line.getHeight()) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return 0;
    }

    // Returns a number indicating the collision of a Ball with the grid
    // 0 - no collision occurred
    // 1 - collision occurred with a vertical wall
    // 2 - collision occurred with a horizontal wall
    public int isCollidingWithGrid(double x, double y, double ballSize) {
        Ellipse2D ballHitBox = new Ellipse2D.Double(x, y, ballSize, ballSize);

        for (Rectangle line : lines) {
            if (ballHitBox.intersects(line)) {
                // Horizontal
                if (y <= line.getY() || y + ballSize >= (line.getY() + line.getHeight())) {
//                    System.out.println(y + " " + line.getY());
//                    System.out.println(y + " " + (line.getY() + line.getHeight()));
                    return 2;
                } else {
                    return 1;
                }
            }
        }

        return 0;
    }

    public void setUserInputs(boolean up, boolean down, boolean left, boolean right, int variant) {
        switch (variant) {
            case PLAYER_ONE -> firstPlayerController.getUserInputs(up, down, left, right);
            case PLAYER_TWO -> secondPlayerController.getUserInputs(up, down, left, right);
        }
    }

    public void playerFire(int variant) {
        switch (variant) {
            case PLAYER_ONE:
                ballManager.addBall(new Ball(1, firstPlayer.getX() + firstPlayer.getPlayerWidth() / 2.0, firstPlayer.getY() + firstPlayer.getPlayerHeight() / 2.0, firstPlayer.getPlayerAngle(), ballSize, this));
                System.out.println("Player one fired!");
                break;
            case PLAYER_TWO:
                ballManager.addBall(new Ball(2, secondPlayer.getX() + secondPlayer.getPlayerWidth() / 2.0, secondPlayer.getY() + secondPlayer.getPlayerHeight() / 2.0, secondPlayer.getPlayerAngle(), ballSize, this));
                System.out.println("Player two fired!");
                break;
        }
    }

    // Returns 1 if the first player won, returns 2 if the second player won, returns 0 if none of them won
    public int gameOver() {
        if (ballManager.collisionWithPlayer(firstPlayer)) {
            firstPlayer.setAlive(false);
            System.out.println("First player dead");
            playSound();
            return 2;
        }

        if (ballManager.collisionWithPlayer(secondPlayer)) {
            secondPlayer.setAlive(false);
            System.out.println("Second player dead");
            playSound();
            return 1;
        }

        return 0;
    }

    // When a player dies, plays an explosion sound
    private void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sound/explosion_sound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("IO error");
        }
    }


    // Stops the game
    public void gameStop() {
        firstPlayerController.endProcess();
        secondPlayerController.endProcess();
        ballManager.endProcess();
    }


}
