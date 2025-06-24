package org.wildloop;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents an event that occurs in the simulation world.
 * Events can be related to animal actions, interactions, or state changes.
 * Each event is associated with a specific world, type, and may involve one or two animals.
 */
public final class Event {
    /** Type of the event */
    private final EventType type;
    /** The world in which the event occurs */
    private final World world;
    /** Event parameters */
    private final Object[] params;
    /** Timestamp when the event occurred */
    private final Instant timestamp;

    /**
     * Constructs an Event that represents an occurrence in the simulation world.
     * Each event is associated with a specific world, event type, and additional
     * parameters that provide context for the event.
     *
     * @param type   the type of the event; must not be null
     * @param world  the world in which the event occurs; can be null if the event is not world-specific
     * @param params an array of additional parameters related to the event; can be null
     *               or empty depending on the type of event
     * @throws NullPointerException if the world or type is null
     */
    private Event(EventType type, World world, Object... params) {
        this.type = Objects.requireNonNull(type, "EventType cannot be null");
        this.world = world;
        type.validate(params);
        this.params = params;
        this.timestamp = Instant.now();
    }

    public static void log(EventType type, World world, Object... params) {
        Event event = new Event(type, world, params);
        EventLogger.publish(event);
    }

    /**
     * Converts the Event object to a string representation.
     * The string includes the timestamp, event type, and a formatted description
     * specific to the event type with its associated parameters.
     *
     * @return a string representation of the Event, formatted as:
     *         "[timestamp] eventType: eventDescription"
     */
    @Override
    public String toString() {
        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .format(LocalDateTime.ofInstant(this.timestamp, ZoneId.systemDefault()));

        if (world != null) {
            return String.format("%s | W-%s T-%d | %s | %s",
                    timestamp, this.world.getId(), this.world.getTurn(),
                    this.type.name(), this.type.getDescription(this.params));
        } else {
            return String.format("%s | SYSTEM | %s | %s",
                    timestamp, this.type.name(),
                    this.type.getDescription(this.params));
        }

    }
}
