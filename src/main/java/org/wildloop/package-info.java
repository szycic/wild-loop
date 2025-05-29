/**
 * <h1>WildLoop</h1>
 *
 * <p>
 * WildLoop to aplikacja symulująca interakcję między drapieżnikami i ofiarami w środowisku
 * opartym na siatce. Projekt demonstruje podstawowe zasady ekosystemu i zachowania zwierząt
 * w kontrolowanym środowisku.
 * </p>
 *
 * <h2>Główne komponenty</h2>
 * <ul>
 *   <li>{@link org.wildloop.StartApp} - Główne okno aplikacji zarządzające interfejsem użytkownika</li>
 *   <li>{@link org.wildloop.SimulationPanel} - Panel odpowiedzialny za wyświetlanie i kontrolę symulacji</li>
 *   <li>{@link org.wildloop.SimulationConfig} - Import domyślnej konfiguracji symulacji</li>
 *   <li>{@link org.wildloop.World} - Reprezentacja świata symulacji i jego logiki</li>
 *   <li>{@link org.wildloop.Position} - Reprezentacja niezmiennej pozycji w dwuwymiarowej przestrzeni</li>
 *   <li>{@link org.wildloop.Direction} - Reprezentacja wektorów przesunięcia</li>
 *   <li>{@link org.wildloop.Animal} - Abstrakcyjna klasa bazowa dla wszystkich zwierząt</li>
 *   <li>{@link org.wildloop.Prey} - Implementacja ofiary</li>
 *   <li>{@link org.wildloop.Predator} - Implementacja drapieżnika</li>
 * </ul>
 *
 * <h2>Funkcje</h2>
 * <ul>
 *   <li>Interaktywna symulacja ekosystemu</li>
 *   <li>Konfigurowalne parametry symulacji (rozmiar świata, liczba zwierząt)</li>
 *   <li>Wizualizacja w czasie rzeczywistym</li>
 *   <li>Statystyki populacji</li>
 *   <li>Możliwość wstrzymania/wznowienia symulacji</li>
 * </ul>
 *
 * <h2>Uruchamianie aplikacji</h2>
 * <p>
 * Aplikacja jest uruchamiana poprzez metodę {@code main}, która tworzy nową instancję {@link org.wildloop.StartApp}
 * w wątku EDT (Event Dispatch Thread) Swinga za pomocą {@link javax.swing.SwingUtilities#invokeLater(Runnable)}.
 * </p>
 *
 * <h2>Wymagania systemowe</h2>
 * <ul>
 *   <li>Java 24 lub nowsza</li>
 *   <li>Środowisko graficzne obsługujące Swing</li>
 * </ul>
 *
 * @author Szymon Cichy, Tomasz Druszcz, Jan Osmęda
 * @version 0.46
 * @see org.wildloop.StartApp
 * @see org.wildloop.SimulationPanel
 * @see org.wildloop.SimulationConfig
 * @see org.wildloop.World
 * @see org.wildloop.Position
 * @see org.wildloop.Direction
 * @see org.wildloop.Animal
 * @see org.wildloop.Prey
 * @see org.wildloop.Predator
 */
package org.wildloop;