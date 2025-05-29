package org.wildloop;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationPanel extends JPanel {
    private final StartApp startApp; // referencja do głównej aplikacji
    private World world; // świat symulacji
    private JLabel[][] gridLabels; // etykiety komórek siatki
    private final JLabel statsLabel; // etykieta statystyk
    private Timer timer; // timer kontrolujący prędkość symulacji
    private final JButton pauseButton; // przycisk pauzy
    private boolean isPaused = false; // flaga przechowująca stan pauzy

    // KONSTRUKTOR budujący interfejs
    public SimulationPanel(StartApp startApp) {
        this.startApp = startApp; // przypisanie przekazanej referencji do pola klasy
        setLayout(new BorderLayout()); // główny układ aplikacji (borderlayout - podział na strefy)

        JPanel gridPanel = new JPanel(new GridLayout(20, 20, 1, 1)); // siatka 20x20 z odstępami 1px
        gridLabels = new JLabel[20][20]; // zainicjalizowanie tablicy etykiet 20x20

        // tworzenie każdej komórki
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER); // tworzenie etykiety z kropką, zawartość jest wyśrodkowana
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // ustawienie szarej obwódki wokół etykiety
                gridLabels[x][y].setOpaque(true); // włączenie przezroczystości (pozwala na wybranie koloru tła)
                gridLabels[x][y].setBackground(Color.WHITE); // ustawienie białego tła etykiety
                gridPanel.add(gridLabels[x][y]); // dodanie do panelu naszej etykiety na konkretnej pozycji
            }
        }

        // PANEL STATYSTYK
        statsLabel = new JLabel("Tura: 0 | Drapieżniki: 0 | Ofiary: 0", SwingConstants.CENTER); // inicjalizacja etykiety statystyk
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ustawienie pustego obramowania z marginesami 10px

        pauseButton = new JButton("Pauza"); // utworzenie przycisku pauzy
        pauseButton.addActionListener(e -> togglePause()); // actionlistener przełączający pauzę po kliknięciu

        JButton backButton = new JButton("Powrót do menu"); // prycisk powrotu do menu
        backButton.addActionListener(e -> {
            stopSimulation(); // zatrzymywanie symulacji
            startApp.showMenu(); // metoda pokazująca panel menu
        });

        // KONTENER NA PRZYCISKI
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // utworzenie panelu na przyciski z wyrównaniem do prawej i odstępami 10x5px
        buttonPanel.add(pauseButton); // dodanie przycisku pauzy do panelu
        buttonPanel.add(backButton); // dodanie przycisku powrotu do panelu

        // PANEL DOLNY
        JPanel bottomPanel = new JPanel(new BorderLayout()); // tworzenie dolnego panelu z układem borderlayout
        bottomPanel.add(statsLabel, BorderLayout.CENTER); // dodanie etykiety statystyk do środkowej części dolnego panelu
        bottomPanel.add(buttonPanel, BorderLayout.EAST); // dodanie panelu przycisków do prawej części dolnego panelu

        add(gridPanel, BorderLayout.CENTER); // dodanie panelu siatki do środkowej części głównego panelu
        add(bottomPanel, BorderLayout.SOUTH); // dodanie dolnego panelu do dolnej części głównego panelu
    }

    // METODA przełączająca stan pauzy
    private void togglePause() {
        isPaused = !isPaused; // negacja aktualnego stanu pauzy
        if (isPaused) { // sprawdzenie czy isPaused == true, jeśli tak
            timer.stop(); // zatrzymaj symulację
            pauseButton.setText("Wznów"); // zmień tekst przycisku z "Pauza na "Wznów"
        } else { // jeśli nie
            timer.start(); // wznów symulację
            pauseButton.setText("Pauza"); // zmień tekst przycisku z "Wznów" na "Pauza"
        }
    }

    // METODA zatrzymująca symulację
    private void stopSimulation() {
        if (timer != null) {
            timer.stop(); // jeśli timer istnieje to go zatrzymaj
        }
        isPaused = false; // przywrócenie flagi do domyślnej wartości
    }

    // METODA ustawiająca parametry symulacji
    public void setSimulationParameters(int size, int preyCount, int predatorCount) {
        this.world = new World(size, size); // tworzenie nowego świata o podanym przez nas rozmiarze
        initializeGrid(size); // inicjalizacja siatki GUI

        // pętla tworząca ofiary
        for (int i = 0; i < preyCount; i++) {
            Position pos = getRandomEmptyPosition(world); // wybranie losowej wolnej pozycji
            if (pos != null) {
                world.addAnimal(new Prey(pos, 100, 15)); // jeśli dana pozycja jest wolna, to tworzy nową ofiarę
            }
        }

        // pętla tworząca drapieżników
        for (int i = 0; i < predatorCount; i++) {
            Position pos = getRandomEmptyPosition(world); // wybranie losowej wolnej pozycji
            if (pos != null) {
                world.addAnimal(new Predator(pos, 100, 20)); // jeśli dana pozycja jest wolna, to tworzy nowego drapieżnika
            }
        }
    }

    // METODA startująca symulację
    public void startSimulation() {
        // sprawdzenie czy świat istnieje
        if (world == null) {
            setSimulationParameters(20, 10, 5); // to ustaw domyślne parametry
        }

        isPaused = false; // restart flagi do domyślnej wartości
        pauseButton.setText("Pauza"); // ustawienie tekstu przycisku na "pauza"

        if (timer != null) {
            timer.stop(); // jeśli istnieje jakikolwiek włączony timer to go zatrzymujemy
        }

        // tworzenie nowego timera z opóźnieniem 500ms
        timer = new Timer(500, e -> {
            // jeśli symulacja nie jest zatrzymana
            if (!isPaused) {
                world.tick(); // wykonanie kroku symulacji
                updateGrid(); // aktualizacja gui
                updateStats(); // aktualizacja statystyk

                // sprawdzanie warunku końca symulacji (czy istnieje jakaś żywa istota)
                if (world.getAnimals().isEmpty()) {
                    stopSimulation(); // zatrzymanie symulacji
                    JOptionPane.showMessageDialog(this, "Symulacja zakończona!"); // wyświetlenie komunikatu o końcu symulacji
                }
            }
        });
        timer.start(); // uruchomienie timera
    }

    // METODA inicjalizująca siatkę
    private void initializeGrid(int size) {
        JPanel gridPanel = (JPanel) getComponent(0); // pobieranie panelu siatki
        gridPanel.removeAll(); // czyszczenie panelu z istniejących komponentów
        gridPanel.setLayout(new GridLayout(size, size)); // ustawienie nowego układu siatki
        gridLabels = new JLabel[size][size]; // zainicjalizowanie tablicy etykiet

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER); // tworzenie etykiety z kropką, zawartość jest wyśrodkowana
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // ustawienie szarej obwódki wokół etykiety
                gridPanel.add(gridLabels[x][y]); // dodanie do panelu naszej etykiety na konkretnej pozycji
            }
        }
        gridPanel.revalidate(); // wymuszenie przebudowy układu
        gridPanel.repaint(); // wymuszanie przerysowania układu
    }

    // METODA aktualizująca siatkę
    private void updateGrid() {
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Animal animal = world.getGrid()[x][y]; // pobranie zwierzęcia z danej komórki
                if (animal == null) {
                    gridLabels[x][y].setText("·"); // jeśli jest pusta wstawiamy kropkę
                } else if (animal instanceof Predator) {
                    gridLabels[x][y].setText("D"); // jeśli jest to drapieżnik wstawiamy D
                } else if (animal instanceof Prey) {
                    gridLabels[x][y].setText("O"); // jeśli jest to ofiara wstawiamy O
                }
            }
        }
    }

    // METODA aktualizująca statystyki
    private void updateStats() {
        int predatorCount = 0; // licznik drapieżników
        int preyCount = 0; // licznik ofiar

        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Predator) {
                predatorCount++; // zliczanie wszystkich drapieżników
            } else if (animal instanceof Prey) {
                preyCount++; // zliczanie wszystkich ofiar
            }
        }

        statsLabel.setText(String.format("Tura: %d | Drapieżniki: %d | Ofiary: %d | Łącznie: %d", world.getTurns(), predatorCount, preyCount, world.getAnimals().size())); // formatowanie tekstu statystyk z aktualnymi danymi
    }

    // METODA losująca pozycję
    private Position getRandomEmptyPosition(World world) {
        // pętla losująca, maksymalnie 100 prób
        for (int attempts = 0; attempts < 100; attempts++) {
            int x = (int) (Math.random() * world.getWidth()); // losowanie pozycji X
            int y = (int) (Math.random() * world.getHeight()); // losowanie pozycji Y
            Position pos = new Position(x, y); // przypisanie wylosowanych danych
            if (world.isCellEmpty(pos)) {
                return pos; // jeśli wylosowana komórka jest pusta to zwracamy naszą pozycję
            }
        }
        return null; // jeśli nie udało się znaleźć wolnego miejsca to nic nie zwracamy
    }

    // METODA przywracająca stan symulacji do początku
    public void resetSimulation() {
        if (timer!= null && timer.isRunning()) {
            timer.stop(); // jeśli timer jest włączony to go zatrzymaj
        }

        isPaused = false; // przywrócenie flagi pauzy do stanu początkowego

        if (world != null) {
            for (Animal animal : new ArrayList<>(world.getAnimals())) {
                world.removeAnimal(animal); // usuwanie wszystkich istniejących zwierząt
            }
            world.resetTurns(); // resetowanie licznika tur
        }

        initializeGrid(world != null ? world.getWidth() : 20); // inicjalizacja siatki GUI
        updateStats(); // zaaktualizowanie statystyk

        if (pauseButton != null) {
            pauseButton.setText("Pauza"); // resetowanie przycisku pauzy
        }
    }
}