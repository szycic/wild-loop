package org.wildloop;

import javax.swing.*;
import java.awt.*;

public class StartApp extends JFrame {

    private final CardLayout cardLayout; // pole przechowujące układ kart do przełączania paneli
    private final JPanel mainPanel; // pole przechowujące główny panel kontenerowy
    private SimulationPanel simulationPanel; // pole przechowujące panel symulacji
    private JTextField sizeField; // pole tekstowe do wprowadzenia rozmiaru świata
    private JTextField preyField; // pole tekstowe do wprowadzenia liczby ofiar
    private JTextField predatorField; // pole tekstowe do wprowadzenia liczby drapieżników

    // KONSTRUKTOR głównej klasy
    public StartApp() {
        this.setTitle("Wild-loop"); // ustawienie tytułu okna
        this.setSize(800, 600); // ustawienie rozmiaru okna na 800x600px
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // wyłączenie aplikacji przy zamknięciu okna
        //this.setResizable(false); // blokowanie możliwości rozszerzania okna
        this.setLocationRelativeTo(null); // centrowanie okna na ekranie

        cardLayout = new CardLayout(); // inicjalizacja układu kart
        mainPanel = new JPanel(cardLayout); // tworzenie głównego panelu z układem kart

        JPanel menuPanel = createMenuPanel(); // tworzenie panelu menu wywołując metodę
        JPanel settingsPanel = createSettingsPanel(); // tworzenie panelu ustawień wywołując metodę
        simulationPanel = new SimulationPanel(this); // inicjalizacja panelu symulacji z przekazaniem referencji

        mainPanel.add(menuPanel, "Menu"); // dodanie panelu menu do głównego panelu
        mainPanel.add(settingsPanel, "Settings"); // dodanie panelu ustawień do głównego panelu
        mainPanel.add(simulationPanel, "Simulation"); // dodanie panelu symulacji do głównego panelu

        this.add(mainPanel); // dodanie głównego panelu do okna
        this.setVisible(true); // włączenie widoczności okna
    }

    // METODA tworząca panel menu
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // utworzenie panelu z układem siatki 3 wierszy, 1 kolumną i odstępami 10px
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); // ustawienie marginesów wewnętrznych panelu (góra, lewo, dół, prawo)

        JButton startButton = new JButton("Start symulacji"); // utworzenie przycisku startu symulacji
        JButton settingsButton = new JButton("Ustawienia"); // utworzenie przycisku ustawień
        JButton exitButton = new JButton("Wyjście"); // utworzenie przycisku wyjścia z aplikacji

        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Simulation"); // po wciśnięciu przycisku przełącz główny panel na panel symulacji
            simulationPanel.startSimulation(); // uruchom symulację
        });
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "Settings")); // po wciśnięciu przycisku ustawień przełącz na panel ustawień
        exitButton.addActionListener(e -> System.exit(0)); // po wciśnięciu przycisku wyjścia wyłącz aplikację

        // dodanie wszystkich przycisków do panelu
        panel.add(startButton);
        panel.add(settingsButton);
        panel.add(exitButton);

        // zwraca cały utworzony panel
        return panel;
    }

    // METODA tworząca panel ustawień
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); // utworzenie panelu z układem siatki 5x2 i odstępami 10px
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // ustawienie marginesów wewnętrznych

        JLabel sizeLabel = new JLabel("Rozmiar świata:"); // etykieta dla rozmiaru świata
        sizeField = new JTextField("20"); // ustawienie domyślnej wartości rozmiaru świata na 20

        JLabel preyLabel = new JLabel("Liczba ofiar:"); // etykieta dla liczby ofiar
        preyField = new JTextField("10"); // ustawienie domyślnej wartości liczby ofiar na 10

        JLabel predatorLabel = new JLabel("Liczba drapieżników:"); // etykieta dla liczby drapieżników
        predatorField = new JTextField("5"); // ustawienie domyślnej wartości liczby drapieżników na 5

        JButton backButton = new JButton("Powrót"); // etykieta dla powrotu do menu
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu")); // po wciśnieciu przycisku powrotu przełącz na główny panel

        JButton saveButton = new JButton("Zapisz ustawienia"); // przycisk do zapisywania ustawień
        saveButton.addActionListener(e -> {
            int size = Integer.parseInt(sizeField.getText()); // pobranie wpisanego rozmiaru świata
            int preyCount = Integer.parseInt(preyField.getText()); // pobranie ilości ofiar
            int predatorCount = Integer.parseInt(predatorField.getText()); // pobranie ilości drapieżników
            simulationPanel.setSimulationParameters(size, preyCount, predatorCount); // ustawienie pobranych wartości w symulacji
            JOptionPane.showMessageDialog(this, "Ustawienia zapisane!"); // wyświetlenie komunikatu o zapisaniu ustawień
        });

        // dodanie wszystkiego do panelu
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(preyLabel);
        panel.add(preyField);
        panel.add(predatorLabel);
        panel.add(predatorField);
        panel.add(saveButton);
        panel.add(backButton);

        // zwraca cały utworzony panel
        return panel;
    }

    // METODA pokazująca menu
    public void showMenu() {
        cardLayout.show(mainPanel, "Menu");
    } //
}
