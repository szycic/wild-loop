package org.wildloop;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationPanel extends JPanel {
    private StartApp startApp;
    private World world;
    private JLabel[][] gridLabels;
    private JLabel statsLabel;
    private Timer timer;
    private JButton pauseButton;
    private boolean isPaused = false;

    public SimulationPanel(StartApp startApp) {
        this.startApp = startApp;
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(20, 20, 1, 1));
        gridLabels = new JLabel[20][20];
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER);
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                gridLabels[x][y].setOpaque(true);
                gridLabels[x][y].setBackground(Color.WHITE);
                gridPanel.add(gridLabels[x][y]);
            }
        }

        statsLabel = new JLabel("Tura: 0 | Drapieżniki: 0 | Ofiary: 0", SwingConstants.CENTER);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pauseButton = new JButton("Pauza");
        pauseButton.addActionListener(e -> togglePause());

        JButton backButton = new JButton("Powrót do menu");
        backButton.addActionListener(e -> {
            stopSimulation();
            startApp.showMenu();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(pauseButton);
        buttonPanel.add(backButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statsLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            pauseButton.setText("Wznów");
        } else {
            timer.start();
            pauseButton.setText("Pauza");
        }
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
        }
        isPaused = false;
    }

    public void setSimulationParameters(int size, int preyCount, int predatorCount) {
        this.world = new World(size, size);
        initializeGrid(size);

        for (int i = 0; i < preyCount; i++) {
            Position pos = getRandomEmptyPosition(world);
            if (pos != null) {
                world.addAnimal(new Prey(pos, 100, 15));
            }
        }

        for (int i = 0; i < predatorCount; i++) {
            Position pos = getRandomEmptyPosition(world);
            if (pos != null) {
                world.addAnimal(new Predator(pos, 100, 20));
            }
        }
    }

    public void startSimulation() {
        if (world == null) {
            setSimulationParameters(20, 10, 5);
        }

        isPaused = false;
        pauseButton.setText("Pauza");

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(500, e -> {
            if (!isPaused) {
                world.tick();
                updateGrid();
                updateStats();

                if (world.getAnimals().isEmpty()) {
                    stopSimulation();
                    JOptionPane.showMessageDialog(this, "Symulacja zakończona!");
                }
            }
        });
        timer.start();
    }

    private void initializeGrid(int size) {
        JPanel gridPanel = (JPanel) getComponent(0);
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(size, size));
        gridLabels = new JLabel[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER);
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                gridPanel.add(gridLabels[x][y]);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateGrid() {
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Animal animal = world.getGrid()[x][y];
                if (animal == null) {
                    gridLabels[x][y].setText("·");
                } else if (animal instanceof Predator) {
                    gridLabels[x][y].setText("D");
                } else if (animal instanceof Prey) {
                    gridLabels[x][y].setText("O");
                }
            }
        }
    }

    private void updateStats() {
        int predatorCount = 0;
        int preyCount = 0;

        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Predator) {
                predatorCount++;
            } else if (animal instanceof Prey) {
                preyCount++;
            }
        }

        statsLabel.setText(String.format("Tura: %d | Drapieżniki: %d | Ofiary: %d | Łącznie: %d",
                world.getTurns(), predatorCount, preyCount, world.getAnimals().size()));
    }

    private Position getRandomEmptyPosition(World world) {
        for (int attempts = 0; attempts < 100; attempts++) {
            int x = (int) (Math.random() * world.getWidth());
            int y = (int) (Math.random() * world.getHeight());
            Position pos = new Position(x, y);
            if (world.isCellEmpty(pos)) {
                return pos;
            }
        }
        return null;
    }
}