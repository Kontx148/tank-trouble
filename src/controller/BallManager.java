package controller;

import modell.Ball;
import modell.Player;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BallManager extends Thread {
    private List<Ball> ballList;
    private boolean running;
    private int ammoCapacity;

    public BallManager(int ammoCapacity) {
        this.ammoCapacity = ammoCapacity;
        ballList = new ArrayList<>();
        running = true;
    }

    public void addBall(Ball ball) {
        // If one player has shot enough balls, then return
        if (ballCountByID(ball.getID()) > ammoCapacity) {
            return;
        }

        double ballX = ball.getX();
        double ballY = ball.getY();
        double xMovement = Ball.BALL_OFFSET * Math.cos(Math.toRadians(ball.getAngle()));
        double yMovement = Ball.BALL_OFFSET * Math.sin(Math.toRadians(ball.getAngle()));
        ball.setLocation(ballX + xMovement, ballY + yMovement);
        ballList.add(ball);
    }

    // Returns the count of ID balls
    private long ballCountByID(int ID) {
        return ballList.stream().filter(ball -> ball.getID() == ID).count();
    }

    @Override
    public void run() {
        while (running) {
            try {
                ballRefresh();
                Thread.sleep(9);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void endProcess() {
        running = false;
    }

    private void ballRefresh() {
        ballList = ballList.stream().peek(ball -> {
                    ball.incrementAge();
                    ball.ballMove();
                })
                .filter(ball -> !ball.isDue())
                .collect(Collectors.toList());
    }

    public void paintBalls(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        for (Ball ball : ballList) {
            double ballSize = ball.getBallSize();
            Ellipse2D ballHitBox = new Ellipse2D.Double(ball.getX(), ball.getY(), ballSize, ballSize);
            g2d.fill(ballHitBox);
        }
    }

    public boolean collisionWithPlayer(Player player) {
        HashMap<String, Line2D> playerHitBox = player.getPlayerHitBox();
        Line2D top = playerHitBox.get("top");
        Line2D left = playerHitBox.get("left");
        Line2D bottom = playerHitBox.get("bottom");
        Line2D right = playerHitBox.get("right");

        for (int i = 0; i < ballList.size(); i++) {
            Ball ball = ballList.get(i);
            if (circleLineIntersect(ball, top) || circleLineIntersect(ball, left) || circleLineIntersect(ball, bottom) || circleLineIntersect(ball, right)) {
                ballList.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean circleLineIntersect(Ball ball, Line2D line) {
        // Circle eq
        double r = ball.getBallSize() / 2;
        double x = ball.getX();
        double y = ball.getY();

        // Line eq
        double x1 = line.getX1();
        double x2 = line.getX2();
        double y1 = line.getY1();
        double y2 = line.getY2();

        // Build a rectangle around the line, and check if it intersects, else calculate line intersection
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);
        Rectangle2D lineBounds = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), width, height);
        double ballSize = ball.getBallSize();
        Ellipse2D ballBounds = new Ellipse2D.Double(ball.getX(), ball.getY(), ballSize, ballSize);
        if (!ballBounds.intersects(lineBounds)) {
            return false;
        }
        double a = y2 - y1;
        double b = x1 - x2;
        double c = y1 * (x2 - x1) - x1 * (y2 - y1);

        double dist = (Math.abs(a * x + b * y + c)) / Math.sqrt(a * a + b * b);

        return dist <= r;
    }
}
