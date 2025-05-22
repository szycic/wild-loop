package org.wildloop;

import javax.swing.*;
import java.awt.*;

public class StartApp extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public StartApp() {
        this.setTitle("Wild-loop");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = createMenuPanel();
        JPanel settingsPanel = createSettingsPanel();
        JPanel simulationPanel = createSimulationPanel();

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(simulationPanel, "Simulation");

        this.add(mainPanel);
        this.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton startButton = new JButton("Start symulacji");
        JButton settingsButton = new JButton("Ustawienia");
        JButton exitButton = new JButton("Wyjście");

        startButton.addActionListener(e -> cardLayout.show(mainPanel, "Simulation"));
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(settingsButton);
        panel.add(exitButton);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel sizeLabel = new JLabel("Rozmiar świata:");
        JTextField sizeField = new JTextField("20");

        JLabel preyLabel = new JLabel("Liczba ofiar:");
        JTextField preyField = new JTextField("10");

        JLabel predatorLabel = new JLabel("Liczba drapieżników:");
        JTextField predatorField = new JTextField("5");

        JButton backButton = new JButton("Powrót");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        JButton saveButton = new JButton("Zapisz ustawienia");

        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(preyLabel);
        panel.add(preyField);
        panel.add(predatorLabel);
        panel.add(predatorField);
        panel.add(saveButton);
        panel.add(backButton);

        return panel;
    }

    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Symulacja w toku...");
        panel.add(label);
        return panel;
    }
}
