package tools;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MapGenerator {
    public static Set<Rectangle> generateLines(int panelSize, int tileSize) {
        Set<Rectangle> lines = new HashSet<>();
        Random random = new Random();

        //Map generation based on binary tree algorithm
        for (int x = 0; x <= panelSize; x += tileSize) {
            for (int y = 0; y <= panelSize; y += tileSize) {
                if (x != 0 && y != 0) {
                    int decider = random.nextInt(2);
                    //generate opening either on the north, or west side
                    if (decider == 1) {
                        Rectangle horizontalLine = new Rectangle(x, y, tileSize, 2);
                        lines.add(horizontalLine);
                    } else {
                        Rectangle verticalLine = new Rectangle(x, y, 2, tileSize);
                        lines.add(verticalLine);
                    }
                }
            }
        }

        //Add borderlines
        Rectangle upperBorder = new Rectangle(0, 0, panelSize, 1);
        Rectangle lowerBorder = new Rectangle(0, panelSize, panelSize, 1);
        Rectangle leftBorder = new Rectangle(0, 0, 1, panelSize);
        Rectangle rightBorder = new Rectangle(panelSize, 0, 1, panelSize);

        lines.add(upperBorder);
        lines.add(lowerBorder);
        lines.add(leftBorder);
        lines.add(rightBorder);

        return lines;
    }
}
