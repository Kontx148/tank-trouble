package main;

import controller.BattleController;
import controller.UserInputs;
import settings.SettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainScreen extends JFrame {
    private CardLayout layout;
    private JPanel cardPanel;
    private SettingsPanel settingsPanel;

    public MainScreen() {
        layout = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(layout);
        add(cardPanel);

        // Main menu buttons
        JPanel mainPanelButtons = new JPanel();
        JButton b1 = new JButton("Play");
        b1.setBackground(Color.black);
        b1.setForeground(Color.white);
        JButton b2 = new JButton("Settings");
        b2.setBackground(Color.black);
        b2.setForeground(Color.white);
        b1.addActionListener(e -> layout.show(cardPanel, "Play"));
        b2.addActionListener(e -> layout.show(cardPanel, "Settings"));
        mainPanelButtons.add(b1, BorderLayout.NORTH);
        mainPanelButtons.add(b2, BorderLayout.NORTH);
        mainPanelButtons.setPreferredSize(new Dimension(600, 50));
        mainPanelButtons.setBackground(Color.GRAY);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(mainPanelButtons, BorderLayout.NORTH);
        mainPanel.add(new MainPanel());
        cardPanel.add(mainPanel, "Main");

        // Game screen
        JLabel firstPlayerLabel = new JLabel("0");
        firstPlayerLabel.setForeground(Color.green);
        JLabel secondPlayerLabel = new JLabel("0");
        secondPlayerLabel.setForeground(Color.red);
        BattleController battleController = new BattleController(firstPlayerLabel, secondPlayerLabel);
        battleController.start();
        JPanel battlePanel = new JPanel();
        JButton b3 = new JButton("Main");
        b3.setBackground(Color.black);
        b3.setForeground(Color.white);
        JButton b4 = new JButton("Settings");
        b4.setBackground(Color.black);
        b4.setForeground(Color.white);
        b3.addActionListener(e -> layout.show(cardPanel, "Main"));
        b4.addActionListener(e -> layout.show(cardPanel, "Settings"));
        battlePanel.setBackground(Color.black);
        battlePanel.add(firstPlayerLabel);
        battlePanel.add(b3);
        battlePanel.add(b4);
        battlePanel.add(secondPlayerLabel);
        battlePanel.add(battleController.getBattleGround());
        cardPanel.add(battlePanel, "Play");


        // Buttons for the settings panel
        JPanel settingsPanelButtons = new JPanel();
        settingsPanelButtons.setBackground(Color.GRAY);
        JButton b5 = new JButton("Main");
        b5.setBackground(Color.black);
        b5.setForeground(Color.white);
        JButton b6 = new JButton("Play");
        b6.setBackground(Color.black);
        b6.setForeground(Color.white);
        b5.addActionListener(e -> layout.show(cardPanel, "Main"));
        b6.addActionListener(e -> layout.show(cardPanel, "Play"));
        settingsPanelButtons.add(b5, BorderLayout.NORTH);
        settingsPanelButtons.add(b6, BorderLayout.NORTH);
        settingsPanelButtons.setPreferredSize(new Dimension(600, 50));

        // Main settings panel
        settingsPanel = new SettingsPanel(battleController);
        JPanel helperSettingsPanel = new JPanel();
        helperSettingsPanel.add(settingsPanelButtons, BorderLayout.NORTH);
        helperSettingsPanel.add(settingsPanel, BorderLayout.CENTER);
        cardPanel.add(helperSettingsPanel, "Settings");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 600, 600);
        UserInputs ui = new UserInputs(battleController.getBattleGround());
        setTitle("Tank Trouble - Remake");
        ImageIcon icon = new ImageIcon("img/icon.png");
        setIconImage(icon.getImage());
        setFocusable(false);
        setResizable(false);
        setJMenuBar(createMenuBar());
        addKeyListener(ui);
        setFocusable(true);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.black);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            int option = jFileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                layout.show(cardPanel, "Settings");
                System.out.println(settingsPanel.getSettings());
                File file = jFileChooser.getSelectedFile();
                try {
                    BufferedWriter wr = new BufferedWriter(new FileWriter(file));
                    wr.write(settingsPanel.getSettings());
                    wr.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            int option = jFileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                layout.show(cardPanel, "Settings");
                File file = jFileChooser.getSelectedFile();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String[] settings = br.readLine().split(" ");
                    br.close();

                    boolean hitboxes = settings[0].equals("true");
                    double playerSpeed = Double.parseDouble(settings[1]);
                    int gridSize = Integer.parseInt(settings[2]);
                    int ammoCapacity = Integer.parseInt(settings[3]);
                    int ballSize = Integer.parseInt(settings[4]);

                    settingsPanel.setSettings(hitboxes, playerSpeed, gridSize, ammoCapacity, ballSize);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    public static void main(String[] args) {
        new MainScreen();
    }
}
