package org.wildloop;

import javax.swing.*;
import java.awt.*;

/**
 * Dziedziczy po {@link JFrame} i służy jako główny kontener interfejsu użytkownika
 * aplikacji. Wykorzystuje układ kart ({@link CardLayout}) do zarządzania i przełączania między różnymi
 * panelami interfejsu, takimi jak menu główne, ustawienia i ekrany symulacji.
 * <p>
 * Klasa zapewnia następujące funkcjonalności:
 * <ul>
 * <li>Początkowa konfiguracja głównego okna aplikacji, w tym rozmiar, tytuł i zachowanie</li>
 * <li>Zarządzanie głównym układem kart, umożliwiając przełączanie między różnymi widokami</li>
 * <li>Tworzenie określonych paneli interfejsu (menu, ustawienia i symulacja)</li>
 * <li>Obsługa interakcji użytkownika, takich jak uruchamianie symulacji, zapisywanie ustawień i wyjście</li>
 * </ul>
 * <p>
 * Aplikacja składa się z trzech głównych paneli:
 * <ul>
 * <li>MenuPanel: Zawiera opcje rozpoczęcia symulacji lub wyjścia z aplikacji</li>
 * <li>SettingsPanel: Pozwala użytkownikowi skonfigurować parametry symulacji, takie jak rozmiar świata,
 *     liczba ofiar i liczba drapieżników</li>
 * <li>SimulationPanel: Wyświetla i zarządza symulacją skonfigurowanego środowiska wirtualnego</li>
 * </ul>
 *
 * @see SimulationPanel
 * @see SimulationConfig
 */
public class StartApp extends JFrame {
    /** Układ kart służący do przełączania między różnymi panelami interfejsu */
    private final CardLayout cardLayout;
    /** Główny panel kontenerowy zawierający wszystkie panele aplikacji */
    private final JPanel mainPanel;
    /** Panel odpowiedzialny za wyświetlanie i zarządzanie symulacją */
    private final SimulationPanel simulationPanel;
    /** Pole tekstowe do wprowadzenia rozmiaru świata symulacji */
    private JTextField sizeField;
    /** Pole tekstowe do wprowadzenia początkowej liczby ofiar */
    private JTextField preyField;
    /** Pole tekstowe do wprowadzenia początkowej liczby drapieżników */
    private JTextField predatorField;

    /**
     * Tworzy nowe okno aplikacji i inicjalizuje wszystkie komponenty interfejsu.
     * Ustawia podstawowe parametry okna takie jak tytuł, rozmiar i zachowanie,
     * oraz tworzy i konfiguruje wszystkie panele aplikacji.
     */
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

    /**
     * Tworzy panel menu głównego aplikacji.
     * Panel zawiera przyciski do rozpoczęcia symulacji i wyjścia z aplikacji,
     * rozmieszczone w układzie pionowym.
     *
     * @return utworzony i skonfigurowany panel menu
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // utworzenie panelu z układem siatki 3 wierszy, 1 kolumną i odstępami 10px
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); // ustawienie marginesów wewnętrznych panelu (góra, lewo, dół, prawo)

        JButton startButton = new JButton("Start symulacji"); // utworzenie przycisku startu symulacji
        JButton exitButton = new JButton("Wyjście"); // utworzenie przycisku wyjścia z aplikacji

        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings"); // po wciśnięciu przycisku przełącz główny panel na panel ustawień
        });
        exitButton.addActionListener(e -> System.exit(0)); // po wciśnięciu przycisku wyjścia wyłącz aplikację

        // dodanie wszystkich przycisków do panelu
        panel.add(startButton);
        panel.add(exitButton);

        // zwraca cały utworzony panel
        return panel;
    }

    /**
     * Tworzy panel konfiguracji parametrów symulacji.
     * Panel zawiera pola do wprowadzenia rozmiaru świata, liczby ofiar i drapieżników,
     * oraz przyciski do zapisania ustawień i powrotu do menu.
     *
     * @return utworzony i skonfigurowany panel ustawień
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); // utworzenie panelu z układem siatki 5x2 i odstępami 10px
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // ustawienie marginesów wewnętrznych

        JLabel sizeLabel = new JLabel("Rozmiar świata:"); // etykieta dla rozmiaru świata
        sizeField = new JTextField(Integer.toString(SimulationConfig.getValue("default.world.size"))); // ustawienie domyślnej wartości rozmiaru świata

        JLabel preyLabel = new JLabel("Liczba ofiar:"); // etykieta dla liczby ofiar
        preyField = new JTextField(Integer.toString(SimulationConfig.getValue("default.prey.count"))); // ustawienie domyślnej wartości liczby ofiar

        JLabel predatorLabel = new JLabel("Liczba drapieżników:"); // etykieta dla liczby drapieżników
        predatorField = new JTextField(Integer.toString(SimulationConfig.getValue("default.predator.count"))); // ustawienie domyślnej wartości liczby drapieżników

        JButton backButton = new JButton("Powrót"); // etykieta dla powrotu do menu
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu")); // po wciśnieciu przycisku powrotu przełącz na główny panel

        JButton saveButton = new JButton("Zapisz ustawienia i rozpocznij symulację"); // przycisk do zapisywania ustawień
        saveButton.addActionListener(e -> {
            int size = Integer.parseInt(sizeField.getText()); // pobranie wpisanego rozmiaru świata
            int preyCount = Integer.parseInt(preyField.getText()); // pobranie ilości ofiar
            int predatorCount = Integer.parseInt(predatorField.getText()); // pobranie ilości drapieżników
            simulationPanel.setSimulationParameters(size, preyCount, predatorCount); // ustawienie pobranych wartości w symulacji
            // JOptionPane.showMessageDialog(this, "Ustawienia zapisane!"); // wyświetlenie komunikatu o zapisaniu ustawień
            cardLayout.show(mainPanel, "Simulation");
            simulationPanel.startSimulation(); // uruchom symulację
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

    /**
     * Przełącza interfejs z powrotem do menu głównego i resetuje stan symulacji.
     * Ta metoda jest wywoływana, gdy symulacja się kończy lub użytkownik
     * chce wrócić do menu głównego.
     */
    public void showMenu() {
        simulationPanel.resetSimulation(); // zrestartowanie danych naszej symulacji do zera
        cardLayout.show(mainPanel, "Menu"); // przełączenie widoku karty na główne menu
    }
}