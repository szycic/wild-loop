/**
 * <h2>WildLoop</h2>
 * WildLoop is an application simulating interaction between predators and prey in a grid-based
 * environment. The project demonstrates basic principles of ecosystem and animal behavior
 * in a controlled environment.
 * <h3>Main Components</h3>
 * <ul>
 *   <li>{@link org.wildloop.StartApp} - Main application window managing the user interface</li>
 *   <li>{@link org.wildloop.SimulationPanel} - Panel responsible for displaying and controlling simulation</li>
 *   <li>{@link org.wildloop.InfoPanel} - Panel displaying information about selected animals</li>
 *   <li>{@link org.wildloop.SimulationConfig} - Import of default simulation configuration</li>
 *   <li>{@link org.wildloop.World} - Representation of a simulation world and its logic</li>
 *   <li>{@link org.wildloop.Position} - Representation of immutable position in 2D space</li>
 *   <li>{@link org.wildloop.Direction} - Representation of displacement vectors</li>
 *   <li>{@link org.wildloop.Animal} - Abstract base class for all animals</li>
 *   <li>{@link org.wildloop.Prey} - Implementation of prey</li>
 *   <li>{@link org.wildloop.Predator} - Implementation of predator</li>
 *   <li>{@link org.wildloop.Event} - Representation of events occurring in the simulation</li>
 *   <li>{@link org.wildloop.EventType} - Enumeration of event types</li>
 *   <li>{@link org.wildloop.EventLogger} - Logger for simulation events</li>
 *   <li>{@link org.wildloop.LogExporter} - Utility for exporting simulation logs</li>
 * </ul>
 * <h3>Features</h3>
 * <ul>
 *   <li>Interactive ecosystem simulation</li>
 *   <li>Configurable simulation parameters (world size, number of animals)</li>
 *   <li>Real-time visualization</li>
 *   <li>Population statistics</li>
 *   <li>Ability to pause/resume simulation</li>
 * </ul>
 * <h3>Running the Application</h3>
 * The application is launched through the {@code main} method which creates a new instance of {@link org.wildloop.StartApp}
 * in Swing's Event Dispatch Thread (EDT) using {@link javax.swing.SwingUtilities#invokeLater(Runnable)}.
 * <h3>System Requirements</h3>
 * <ul>
 *   <li>Java 24 or newer</li>
 *   <li>Graphical environment supporting Swing</li>
 * </ul>
 *
 * @author Szymon Cichy, Tomasz Druszcz, Jan Osmęda
 * @version 0.6.2
 * @see org.wildloop.StartApp
 * @see org.wildloop.SimulationPanel
 * @see org.wildloop.InfoPanel
 * @see org.wildloop.SimulationConfig
 * @see org.wildloop.World
 * @see org.wildloop.Position
 * @see org.wildloop.Direction
 * @see org.wildloop.Animal
 * @see org.wildloop.Prey
 * @see org.wildloop.Predator
 * @see org.wildloop.Event
 * @see org.wildloop.EventType
 * @see org.wildloop.EventLogger
 * @see org.wildloop.LogExporter
 */
package org.wildloop;