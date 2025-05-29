package org.wildloop;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Klasa SimulationPanel reprezentuje główny graficzny interfejs użytkownika dla symulacji
 * składającej się ze świata opartego na siatce zamieszkałego przez drapieżniki i ofiary,
 * oraz zapewnia kontrolki do zarządzania i wyświetlania stanu symulacji.
 * </p>
 * <p>
 * Ta klasa rozszerza JPanel i jest zaprojektowana do integracji z frameworkiem Swing GUI.
 * Zarządza siatką symulacji, wyświetlaniem statystyk i zapewnia interakcję użytkownika
 * poprzez przyciski takie jak pauza i powrót.
 * </p>
 */
public class SimulationPanel extends JPanel {
    private final StartApp startApp; // referencja do głównej aplikacji
    private World world; // świat symulacji
    private JLabel[][] gridLabels; // etykiety komórek siatki
    private final JLabel statsLabel; // etykieta statystyk
    private Timer timer; // timer kontrolujący prędkość symulacji
    private final JButton pauseButton; // przycisk pauzy
    private boolean isPaused = false; // flaga przechowująca stan pauzy

    /**
     * Konstruktor panelu symulacji dla aplikacji. Ten panel zawiera
     * układ siatki reprezentujący obszar symulacji, wyświetlacz statystyk oraz
     * przyciski kontrolne do wstrzymywania symulacji i powrotu do menu głównego.
     *
     * @param startApp główna instancja aplikacji używana do nawigacji powrotnej do
     *                 menu głównego i kontroli symulacji.
     */
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

    /**
     * <p>
     * Przełącza stan wstrzymania symulacji.
     * </p>
     * <p>
     * Jeśli symulacja aktualnie działa, metoda wstrzymuje symulację
     * poprzez zatrzymanie timera i zmianę tekstu przycisku pauzy, aby wskazać
     * że symulacja może zostać wznowiona.
     * </p>
     * <p>
     * Jeśli symulacja jest obecnie wstrzymana, metoda wznawia symulację
     * poprzez uruchomienie timera i aktualizację tekstu przycisku wskazującą
     * że symulacja może zostać ponownie wstrzymana.
     * </p>
     * <p>
     * Stan wstrzymania jest śledzony przez pole {@code isPaused}.
     * </p>
     */
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

    /**
     * <p>
     * Zatrzymuje symulację poprzez zatrzymanie timera i resetowanie stanu wstrzymania.
     * </p>
     * <p>
     * Metoda sprawdza, czy timer symulacji jest aktywny. Jeśli tak, zatrzymuje timer,
     * aby zapobiec dalszym aktualizacjom symulacji. Dodatkowo upewnia się, że stan
     * wstrzymania jest ustawiony na false, wskazując że symulacja nie jest już wstrzymana.
     * </p>
     * <p>
     * Metoda jest wywoływana podczas procesu zakończenia lub resetowania symulacji
     * w celu przywrócenia jej do stanu domyślnego.
     * </p>
     */
    private void stopSimulation() {
        if (timer != null) {
            timer.stop(); // jeśli timer istnieje to go zatrzymaj
        }
        isPaused = false; // przywrócenie flagi do domyślnej wartości
    }

    /**
     * Konfiguruje parametry symulacji poprzez ustawienie wymiarów symulowanego świata,
     * inicjalizację siatki dla interfejsu graficznego oraz wypełnienie symulacji
     * zwierzętami typu ofiara i drapieżnik na podstawie podanych liczb.
     *
     * @param size          rozmiar kwadratowego świata (np. 10 tworzy siatkę 10x10)
     * @param preyCount     liczba zwierząt typu ofiara do wygenerowania w symulacji
     * @param predatorCount liczba zwierząt typu drapieżnik do wygenerowania w symulacji
     */
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

    /**
     * <p>
     * Uruchamia symulację poprzez inicjalizację lub resetowanie niezbędnych komponentów
     * i uruchomienie głównej pętli symulacji.
     * </p>
     * <p>
     * Metoda sprawdza, czy świat symulacji jest zainicjalizowany. Jeśli nie,
     * ustawia domyślne parametry symulacji za pomocą {@link #setSimulationParameters(int, int, int)}.
     * Upewnia się, że symulacja nie jest wstrzymana poprzez zresetowanie flagi pauzy
     * i aktualizuje przycisk sterujący, aby wyświetlał "Pauza".
     * </p>
     * <p>
     * Główna pętla symulacji jest zarządzana przez {@link Timer} z
     * opóźnieniem 500 milisekund. Każde tknięcie timera wykonuje następujące czynności:
     * <ul>
     * <li>Rozwija stan symulacji za pomocą {@link World#tick()}.</li>
     * <li>Aktualizuje siatkę GUI za pomocą {@link #updateGrid()}.</li>
     * <li>Aktualizuje statystyki symulacji za pomocą {@link #updateStats()}.</li>
     * </ul>
     * </p>
     * <p>
     * Jeśli symulacja osiągnie warunek końcowy, na przykład brak pozostałych zwierząt
     * w świecie, symulacja zostaje zatrzymana za pomocą {@link #stopSimulation()},
     * a użytkownikowi wyświetlane jest powiadomienie.
     * </p>
     */
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

    /**
     * Inicjalizuje siatkę symulacji o określonym rozmiarze.
     * Ta metoda tworzy i konfiguruje układ siatki z etykietami dla kwadratowej siatki,
     * aktualizuje interfejs graficzny i resetuje poprzednie komponenty siatki.
     *
     * @param size rozmiar kwadratowej siatki do zainicjalizowania (np. 10 tworzy siatkę 10x10)
     */
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

    /**
     * <p>
     * Aktualizuje wizualną reprezentację siatki symulacji.
     * </p>
     * <p>
     * Metoda iteruje przez każdą komórkę w siatce świata symulacji i aktualizuje graficzne
     * etykiety odpowiadające każdej komórce na podstawie jej aktualnej zawartości. W szczególności:
     * <ul>
     *     <li>Jeśli komórka jest pusta (null), ustawia tekst etykiety na "·" aby wskazać puste miejsce</li>
     *     <li>Jeśli komórka zawiera instancję {@code Predator}, ustawia tekst etykiety na "D"</li>
     *     <li>Jeśli komórka zawiera instancję {@code Prey}, ustawia tekst etykiety na "O"</li>
     * </ul>
     * </p>
     */
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

    /**
     * <p>
     * Aktualizuje statystyki symulacji poprzez zliczanie liczby drapieżników i ofiar
     * w symulowanym świecie. Metoda pobiera listę zwierząt, rozróżnia drapieżniki
     * i ofiary na podstawie ich klas oraz oblicza ich sumy.
     * </p>
     * <p>
     * Następnie aktualizuje wyświetlacz statystyk o aktualny numer tury, liczbę
     * drapieżników, ofiar oraz łączną liczbę zwierząt.
     * </p>
     */
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

    /**
     * Generuje losową pustą pozycję w podanym świecie.
     * Metoda próbuje znaleźć niezajętą pozycję w granicach świata.
     * Wykonuje maksymalnie 100 prób znalezienia pustej komórki.
     * Jeśli po 100 próbach nie zostanie znaleziona żadna pusta komórka,
     * zwraca null.
     *
     * @param world świat, w którym należy znaleźć losową pustą pozycję
     * @return losowo wybrana pusta pozycja w świecie lub null jeśli takiej
     * pozycji nie znaleziono po 100 próbach
     */
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

    /**
     * <p>
     * Przywraca symulację do stanu początkowego poprzez zatrzymanie wszystkich aktywnych procesów,
     * wyczyszczenie istniejących danych symulacji oraz reinicjalizację widoku i niezbędnych komponentów.
     * </p>
     * <p>
     * Funkcjonalność:
     * <ul>
     * <li>Zatrzymuje timer jeśli jest aktualnie uruchomiony</li>
     * <li>Resetuje stan wstrzymania do false</li>
     * <li>Usuwa wszystkie zwierzęta ze świata symulacji i zeruje licznik tur</li>
     * <li>Reinicjalizuje siatkę graficzną reprezentującą obszar symulacji</li>
     * <li>Aktualizuje statystyki symulacji aby odzwierciedlały zresetowany stan</li>
     * <li>Przywraca etykietę przycisku pauzy do stanu domyślnego</li>
     * </ul>
     * </p>
     * <p>
     * Warunki wstępne:
     * <ul>
     * <li>Obiekt 'world', jeśli nie jest null, musi prawidłowo zarządzać swoją listą zwierząt i licznikiem tur</li>
     * <li>Komponenty siatki graficznej i statystyk muszą pozwalać na reinicjalizację</li>
     * </ul>
     * </p>
     * <p>
     * Warunki końcowe:
     * <ul>
     * <li>Symulacja jest ustawiona w czystym stanie, gotowa do rozpoczęcia nowego przebiegu symulacji</li>
     * </ul>
     * </p>
     */
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