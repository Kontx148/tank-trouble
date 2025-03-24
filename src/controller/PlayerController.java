package controller;

import modell.Player;
import view.BattleGround;

public class PlayerController extends Thread {
    private Player player;
    private BattleGround battleGround;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean running;

    public PlayerController(Player player, BattleGround battleGround) {
        this.player = player;
        this.battleGround = battleGround;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            playerMove();
            try {
                Thread.sleep(9);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void endProcess() {
        running = false;
    }

    private void playerMove() {
        double newX = player.getX();
        double newY = player.getY();
        double newAngle = player.getPlayerAngle();

        if (leftPressed) {
            newAngle -= Player.PLAYER_TURN;
        }
        if (rightPressed) {
            newAngle += Player.PLAYER_TURN;
        }

        double playerSpeed = player.getPlayerSpeed();

        double xMovement = playerSpeed * Math.cos(Math.toRadians(newAngle));
        double yMovement = playerSpeed * Math.sin(Math.toRadians(newAngle));
        if (upPressed) {
            newX += xMovement;
            newY += yMovement;
        }
        if (downPressed) {
            newX -= xMovement;
            newY -= yMovement;
        }
        //System.out.println("X : " + player.getX() + " Y : " + player.getY() + " Deg " + player.getPlayerAngle() + " " + xMovement + " " + yMovement);

        int collidedWith = battleGround.isCollidingWithGrid(player);
        switch (collidedWith) {
            case 1:
                newX = player.getX();
                break;
            case 2:
                newY = player.getY();
                break;
        }
        //System.out.println(battleGround.isCollidingWithGrid(player));
        newAngle = (newAngle + 360) % 360;

        player.setAngle(newAngle);
        player.setLocation(newX, newY);

    }

    public void getUserInputs(boolean up, boolean down, boolean left, boolean right) {
        upPressed = up;
        downPressed = down;
        leftPressed = left;
        rightPressed = right;
    }
}