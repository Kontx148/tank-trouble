package modell;

import view.BattleGround;

public class Ball {
    public static final double BALL_OFFSET = 15; // distance from the firing players center
    private final double BALL_MOVE = 3; // Ball speed
    private final int LIFESPAN = 1000; // Ball life duration
    private double ballSize; // Ball size, can be changed in settings
    private int age = 0;
    private double x;
    private double y;
    private int ID; // Who fired the ball
    private double angle;
    private BattleGround battleGround;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getBallSize() {
        return ballSize;
    }

    public int getID() {
        return ID;
    }

    public void incrementAge() {
        age++;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDue() {
        return age > LIFESPAN;
    }

    public Ball(int ID, double x, double y, double angle, double ballSize, BattleGround battleGround) {
        this.ID = ID;
        this.ballSize = ballSize;
        this.x = x - ballSize / 2.0;
        this.y = y - ballSize / 2.0;
        this.angle = angle;
        this.battleGround = battleGround;
        ballMove();
    }

    // Moves the ball
    public void ballMove() {
        double xMovement = BALL_MOVE * Math.cos(Math.toRadians(angle));
        double yMovement = BALL_MOVE * Math.sin(Math.toRadians(angle));

        int collidedWith = battleGround.isCollidingWithGrid(x + xMovement, y + yMovement, ballSize);
        if (collidedWith != 0) {
            if (collidedWith == 2) {
                angle = 360 - angle;
            } else {
                angle = 180 - angle;
            }
            xMovement = BALL_MOVE * Math.cos(Math.toRadians(angle));
            yMovement = BALL_MOVE * Math.sin(Math.toRadians(angle));
        }

        x += xMovement;
        y += yMovement;
    }

}
