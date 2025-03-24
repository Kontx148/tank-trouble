package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainPanel extends JPanel {
    private JLabel title;
    private BufferedImage titleImage;
    private JLabel creator;

    public MainPanel() {
        setPreferredSize(new Dimension(600, 550));
        setBackground(Color.GRAY);
        setLayout(null);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/Coburn-73Zl.ttf"));
            customFont = customFont.deriveFont(38f);
            title = new JLabel("TANK TROUBLE");
            title.setBounds(100, 0, 600, 100);
            title.setFont(customFont);
            add(title);

            titleImage = ImageIO.read(new File("img/wallpaper.png"));

            creator = new JLabel("Veres Norbert");
            customFont = customFont.deriveFont(12f);
            creator.setBounds(400, 333, 100, 100);
            creator.setFont(customFont);
            add(creator);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (titleImage != null) {
            g.drawImage(titleImage, 100, 100, titleImage.getWidth(), titleImage.getHeight(), this);
        }
    }
}
