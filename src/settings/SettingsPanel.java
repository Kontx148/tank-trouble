package settings;

import controller.BattleController;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class SettingsPanel extends JPanel {
    private final BattleController battleController;
    private int gridSize = 10;
    private boolean hitBoxes = false;
    private int ballSize = 5;
    private int ammoCapacity = 5;
    private double playerSpeed = 1;

    private final JCheckBox hitboxCheckBox;
    private final JSlider playerSpeedSlider;
    private final JSlider gridSizeSlider;
    private final JSlider ammoCapacitySlider;
    private final JSlider ballSizeSlider;

    public SettingsPanel(BattleController battleController) {
        this.battleController = battleController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //  Player hitbox setting
        JPanel hitboxPanel = new JPanel();
        hitboxCheckBox = new JCheckBox();
        JLabel hitboxLabel = new JLabel("Show hitboxes");
        hitboxCheckBox.addItemListener(e -> {
            hitBoxes = e.getStateChange() == ItemEvent.SELECTED;
            updateController();
        });
        hitboxPanel.add(hitboxLabel);
        hitboxPanel.add(hitboxCheckBox);
        add(hitboxPanel);

        // Player speed setting
        JPanel playerSpeedPanel = new JPanel();
        playerSpeedSlider = new JSlider(5, 25, 10);
        JLabel playerSpeedSliderValue = new JLabel(String.valueOf(playerSpeed));
        playerSpeedSlider.addChangeListener(e -> {
            playerSpeed = playerSpeedSlider.getValue() / 10.0;
            playerSpeedSliderValue.setText((String.valueOf(playerSpeed)));
            updateController();
        });
        playerSpeedPanel.add(new JLabel("Player Speed"));
        playerSpeedPanel.add(playerSpeedSlider);
        playerSpeedPanel.add(playerSpeedSliderValue);
        add(playerSpeedPanel);

        // Grid Size settings
        JPanel gridSizePanel = new JPanel();
        gridSizeSlider = new JSlider(8, 12, 10);
        JLabel gridSizeLabel = new JLabel(String.valueOf(gridSize));
        gridSizeSlider.addChangeListener(e -> {
            gridSize = gridSizeSlider.getValue();
            gridSizeLabel.setText(String.valueOf(gridSize));
            System.out.println("Grid size changed");
            updateController();
        });
        gridSizePanel.add(new JLabel("Grid Size"));
        gridSizePanel.add(gridSizeSlider);
        gridSizePanel.add(gridSizeLabel);
        add(gridSizePanel);

        // Ammo capacity settings
        JPanel ammoCapacityPanel = new JPanel();
        ammoCapacitySlider = new JSlider(1, 10, 5);
        JLabel ammoCapacityLabel = new JLabel(String.valueOf(ammoCapacity));
        ammoCapacitySlider.addChangeListener(e -> {
            ammoCapacity = ammoCapacitySlider.getValue();
            ammoCapacityLabel.setText(String.valueOf(ammoCapacity));
            updateController();
        });
        ammoCapacityPanel.add(new JLabel("Ammo Capacity"));
        ammoCapacityPanel.add(ammoCapacitySlider);
        ammoCapacityPanel.add(ammoCapacityLabel);
        add(ammoCapacityPanel);

        // Ball size setting
        JPanel ballSizePanel = new JPanel();
        ballSizeSlider = new JSlider(3, 10, 5);
        JLabel ballSizeLabel = new JLabel(String.valueOf(ballSize));
        ballSizeSlider.addChangeListener(e -> {
            ballSize = ballSizeSlider.getValue();
            ballSizeLabel.setText(String.valueOf(ballSize));
            updateController();
        });
        ballSizePanel.add(new JLabel("Ball Size"));
        ballSizePanel.add(ballSizeSlider);
        ballSizePanel.add(ballSizeLabel);
        add(ballSizePanel);

    }

    public String getSettings() {
        return hitBoxes + " " + playerSpeed + " " + gridSize + " " + ammoCapacity + " " + ballSize;
    }

    public void setSettings(boolean hitBoxes, double playerSpeed, int gridSize, int ammoCapacity, int ballSize) {
        hitboxCheckBox.setSelected(hitBoxes);
        playerSpeedSlider.setValue((int) (playerSpeed * 10));
        gridSizeSlider.setValue(gridSize);
        ammoCapacitySlider.setValue(ammoCapacity);
        ballSizeSlider.setValue(ballSize);
    }

    public void updateController() {
        battleController.applySettings(gridSize, hitBoxes, ballSize, ammoCapacity, playerSpeed);
    }


}
