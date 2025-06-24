package org.wildloop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * The EventLogger class is responsible for managing and logging events
 * that occur within the simulation world. It maintains a list of listeners
 * that can handle these events, allowing for modular and extensible event
 * handling.
 * </p>
 * <p>
 * This class provides a static list of listeners, which can be registered
 * to respond to various events in the simulation. Each listener is a
 * {@code Consumer<Event>} that processes an {@link Event} when it occurs.
 * </p>
 *
 * @see Event
 * @see EventType
 * @see LogExporter
 */
public class EventLogger {
    /**
     * <p>
     * A list maintaining all registered listeners for handling events.
     * Each listener is a {@code Consumer<Event>} that processes an {@link Event}
     * when it occurs. Events can represent actions, changes, or interactions
     * within the simulation world.
     * </p>
     * <p>
     * This list allows multiple listeners to be notified whenever an event
     * is triggered, enabling modular and extensible event handling.
     * </p>
     * <p>
     * The listeners are stored as a static and thread-safe {@link ArrayList},
     * and are meant to be managed and invoked by the containing class.
     * </p>
     */
    private static final List<Consumer<Event>> listeners = new ArrayList<>();

    /**
     * Registers a new listener to handle simulation events. The provided listener is added
     * to the static list of listeners that are notified when an event occurs.
     * Each listener must be a {@code Consumer<Event>}, where {@link Event} contains
     * details about the occurrence in the simulation.
     *
     * @param listener the listener to register for handling events. Must not be null.
     *                 If the listener is null, it will not be added to the list.
     */
    public static void subscribe(Consumer<Event> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Unsubscribes a previously registered listener from handling events.
     * The provided listener is removed from the static list of listeners,
     * so it will no longer receive notifications for future events.
     *
     * @param listener the listener to remove from the event handling list.
     *                 If the listener is null, no action is taken.
     */
    public static void unsubscribe(Consumer<Event> listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Publishes an event to all registered listeners. Each listener will
     * receive the event and can process it accordingly. If the event is null,
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param event the event to publish; must not be null.
     *              If null, an exception is thrown.
     * @throws IllegalArgumentException if the event is null.
     */
    static void publish(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        for (Consumer<Event> listener : listeners) {
            listener.accept(event);
        }
    }
}
