package modell;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Player {
    public static final int PLAYER_TURN = 2;
    private double x;
    private double y;
    private double angle; //rotation angle in radians

    // The Tank Image is 28x19, but the gun will be excluded from the player's hitbox
    private final int width = 23;
    private final int height = 19;
    private BufferedImage playerImage;
    private ImageIcon explosionImage;
    private boolean isAlive;
    private double playerSpeed;
    private boolean hitBoxes;

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    // Player constructor, x,y - position on screen, variant - player image variant
    public Player(double playerSpeed, boolean hitBoxes, double x, double y, double angle, int variant) {
        this.hitBoxes = hitBoxes;
        this.playerSpeed = playerSpeed;
        isAlive = true;
        this.x = x;
        this.y = y;
        this.angle = Math.toRadians(angle);
        try {
            switch (variant) {
                case 1:
                    playerImage = ImageIO.read(new File("img/greenTank.png"));
                    break;
                case 2:
                    playerImage = ImageIO.read(new File("img/redTank.png"));
                    break;
            }
            explosionImage = new ImageIcon("img/explosion.gif");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getPlayerWidth() {
        return width;
    }

    public int getPlayerHeight() {
        return height;
    }

    public double getPlayerAngle() {
        return Math.toDegrees(angle);
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public void setAngle(double degree) {
        angle = Math.toRadians(degree);
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawPlayer(Graphics g) {
        // Draws the player on the screen, based on its angle
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(angle, (double) width / 2, (double) height / 2);
        Graphics2D g2d = (Graphics2D) g;
        if (!isAlive) {
            g2d.drawImage(explosionImage.getImage(), at, null);
            return;
        }
        g2d.drawImage(playerImage, at, null);


        // If the hitboxes setting, is on paint also the players hitbox
        if (hitBoxes) {
            g2d.setColor(Color.RED);
            HashMap<String, Line2D> playerHitBox = getPlayerHitBox();
            g2d.draw(playerHitBox.get("top"));
            g2d.draw(playerHitBox.get("left"));
            g2d.draw(playerHitBox.get("bottom"));
            g2d.draw(playerHitBox.get("right"));
        }
    }

    public HashMap<String, Line2D> getPlayerHitBox() {
        double centerX = x + width / 2.0; // Center of the rectangle
        double centerY = y + height / 2.0;

        // Top-left corner
        double x1 = centerX + (width / 2.0) * Math.cos(angle) - (height / 2.0) * Math.sin(angle);
        double y1 = centerY + (width / 2.0) * Math.sin(angle) + (height / 2.0) * Math.cos(angle);

        // Top-right corner
        double x2 = centerX + (width / 2.0) * Math.cos(angle) + (height / 2.0) * Math.sin(angle);
        double y2 = centerY + (width / 2.0) * Math.sin(angle) - (height / 2.0) * Math.cos(angle);

        // Bottom-left corner
        double x3 = centerX - (width / 2.0) * Math.cos(angle) - (height / 2.0) * Math.sin(angle);
        double y3 = centerY - (width / 2.0) * Math.sin(angle) + (height / 2.0) * Math.cos(angle);

        // Bottom-right corner
        double x4 = centerX - (width / 2.0) * Math.cos(angle) + (height / 2.0) * Math.sin(angle);
        double y4 = centerY - (width / 2.0) * Math.sin(angle) - (height / 2.0) * Math.cos(angle);

        HashMap<String, Line2D> hitbox = new HashMap<>();
        hitbox.put("top", new Line2D.Double(x1, y1, x2, y2));
        hitbox.put("left", new Line2D.Double(x1, y1, x3, y3));
        hitbox.put("bottom", new Line2D.Double(x3, y3, x4, y4));
        hitbox.put("right", new Line2D.Double(x4, y4, x2, y2));

        return hitbox;
    }

}
