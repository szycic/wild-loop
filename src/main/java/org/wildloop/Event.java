package org.wildloop;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents an event that occurs in the simulation world.
 * Events can be related to animal actions, interactions, or state changes.
 * Each event is associated with a specific world, type, and may involve one or two animals.
 */
public final class Event {
    /** World in which the event occurred */
    private final World world;
    /** Type of the event */
    private final EventType type;
    /** Event parameters */
    private final Object[] params;
    /** Timestamp when the event occurred */
    private final Instant timestamp;
    private final int turn;

    /**
     * Constructs an Event that represents an occurrence in the simulation world.
     * Each event is associated with a specific world, event type, and additional
     * parameters that provide context for the event.
     *
     * @param world the simulation world where the event occurred; must not be null
     * @param type the type of the event; must not be null
     * @param params an array of additional parameters related to the event; can be null
     *               or empty depending on the type of event
     * @throws NullPointerException if the world or type is null
     */
    private Event(World world, EventType type, Object... params) {
        this.world = Objects.requireNonNull(world, "World cannot be null");
        this.type = Objects.requireNonNull(type, "EventType cannot be null");
        validateParams(params);
        this.params = params;
        this.timestamp = Instant.now();
        this.turn = this.world.getTurn();
    }

    /**
     * Validates the parameters provided for the event based on the event type.
     * Ensures that the correct number and types of parameters are provided
     * according to the requirements of the event type.
     *
     * @param params the parameters to validate; must match the requirements of the event type
     * @throws IllegalArgumentException if the number or types of parameters do not match
     *                                  the requirements for the event type
     */
    private void validateParams (Object... params) {
        EventAdditionalParams paramType = type.getAdditionalParams();
        if (params.length != paramType.getParamCount()) {
            throw new IllegalArgumentException("Event type " + type.name() + " requires " + paramType.getParamCount() + " parameters, but " + params.length + " were provided");
        }

        switch (paramType) {
            case NONE -> {
                if (params.length != 0) {
                    throw new IllegalArgumentException("Event type " + type.name() + " does not require any parameters, but " + params.length + " were provided");
                }
            }
            case ANIMAL -> {
                if (params.length != 1) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires a single Animal parameter, but " + params.length + " were provided");
                }
                if (!(params[0] instanceof Animal)) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires an Animal parameter, but received invalid type");
                }
            }
            case PARENTS_OFFSPRING -> {
                if (params.length != 3) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires three Animal parameters (two parents and one offspring), but " + params.length + " were provided");
                }
                if (!(params[0] instanceof Animal) || !(params[1] instanceof Animal) || !(params[2] instanceof Animal)) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires three Animal parameters, but received invalid types");
                }
            }
            case PREDATOR_PREY -> {
                if (params.length != 2) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires two Animal parameters (predator and prey), but " + params.length + " were provided");
                }
                if (!(params[0] instanceof Predator) || !(params[1] instanceof Prey)) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires a Predator and Prey parameter in that order");
                }
            }
            case PREY_PREDATOR -> {
                if (params.length != 2) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires two Animal parameters (prey and predator), but " + params.length + " were provided");
                }
                if (!(params[0] instanceof Prey) || !(params[1] instanceof Predator)) {
                    throw new IllegalArgumentException("Event type " + type.name() + " requires a Prey and Predator parameter in that order");
                }
            }
        }
    }

    /**
     * Logs an event in the specified world with the given type and parameters.
     * This method creates a new Event instance and adds it to the world's event list.
     *
     * @param world the world where the event occurred; must not be null
     * @param type the type of the event; must not be null
     * @param params additional parameters related to the event; can be null or empty
     * @throws NullPointerException if the world or type is null
     */
    public static void log(World world, EventType type, Object... params) {
        Event event = new Event(world, type, params);
        world.addEvent(event);
    }

    /**
     * Returns a string representation of the event, including the timestamp, event type,
     * involved animals, position, and additional details when available.
     * The format of the returned string ensures that all relevant event information is
     * displayed in a structured and readable manner.
     *
     * @return a string representing the event details
     */
    @Override
    public String toString() {
        return String.format("[%s] %s (Turn %d): %s",
                this.timestamp, this.world, this.turn,
                this.type.getDescription(this.world, this.params));
    }
}
