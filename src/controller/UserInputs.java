package controller;

import view.BattleGround;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInputs implements KeyListener {
    private boolean[] upPressed;
    private boolean[] downPressed;
    private boolean[] leftPressed;
    private boolean[] rightPressed;
    private BattleGround battleGround;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> upPressed[0] = true;
            case KeyEvent.VK_DOWN -> downPressed[0] = true;
            case KeyEvent.VK_LEFT -> leftPressed[0] = true;
            case KeyEvent.VK_RIGHT -> rightPressed[0] = true;
            case KeyEvent.VK_E -> upPressed[1] = true;
            case KeyEvent.VK_D -> downPressed[1] = true;
            case KeyEvent.VK_S -> leftPressed[1] = true;
            case KeyEvent.VK_F -> rightPressed[1] = true;
            case KeyEvent.VK_M -> battleGround.playerFire(BattleGround.PLAYER_ONE);
            case KeyEvent.VK_Q -> battleGround.playerFire(BattleGround.PLAYER_TWO);
        }
        battleGround.setUserInputs(upPressed[0], downPressed[0], leftPressed[0], rightPressed[0], BattleGround.PLAYER_ONE);
        battleGround.setUserInputs(upPressed[1], downPressed[1], leftPressed[1], rightPressed[1], BattleGround.PLAYER_TWO);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> upPressed[0] = false;
            case KeyEvent.VK_DOWN -> downPressed[0] = false;
            case KeyEvent.VK_LEFT -> leftPressed[0] = false;
            case KeyEvent.VK_RIGHT -> rightPressed[0] = false;
            case KeyEvent.VK_E -> upPressed[1] = false;
            case KeyEvent.VK_D -> downPressed[1] = false;
            case KeyEvent.VK_S -> leftPressed[1] = false;
            case KeyEvent.VK_F -> rightPressed[1] = false;
        }
        battleGround.setUserInputs(upPressed[0], downPressed[0], leftPressed[0], rightPressed[0], BattleGround.PLAYER_ONE);
        battleGround.setUserInputs(upPressed[1], downPressed[1], leftPressed[1], rightPressed[1], BattleGround.PLAYER_TWO);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    public UserInputs(BattleGround battleGround) {
        this.battleGround = battleGround;
        upPressed = new boolean[2];
        downPressed = new boolean[2];
        leftPressed = new boolean[2];
        rightPressed = new boolean[2];
    }
}
