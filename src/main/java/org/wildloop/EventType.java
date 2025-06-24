package org.wildloop;

/**
 * Enum representing various types of events that can occur during the simulation.
 * Each event type has a specific format for its description and may require certain parameters.
 * The enum provides methods to generate formatted descriptions and validate parameters.
 *
 * @see Event
 * @see EventLogger
 * @see LogExporter
 */
public enum EventType {
    /** Signals the start of the simulation */
    SIMULATION_START("Simulation started"),
    /** Signals pausing of the simulation */
    SIMULATION_PAUSE("Simulation paused"),
    /** Signals resuming of the simulation */
    SIMULATION_RESUME("Simulation resumed"),
    /** Signals the end of the simulation */
    SIMULATION_END("Simulation ended"),
    /** Indicates completion of a simulation turn with turn number */
    SIMULATION_TURN("Turn is completed"),

    /** Records birth/spawn of a new animal */
    SPAWN("%s spawned at %s", Animal.class),
    /** Records death of an animal after being eaten by another */
    DIE_EATEN("%s died at %s after being eaten by %s at %s", Prey.class, Predator.class),
    /** Records death of an animal due to energy depletion */
    DIE_ENERGY("%s died at %s due to energy depletion", Animal.class),
    /** Records death of an animal due to old age */
    DIE_AGE("%s died at %s due to old age", Animal.class),

    /** Records movement of an animal to a new position */
    MOVE("%s moved to %s", Animal.class),
    /** Records reproduction between two animals creating offspring */
    REPRODUCE("%s at %s reproduced, creating offspring %s at %s", Animal.class, Animal.class),
    /** Records a prey animal grazing and gaining energy */
    EAT_GRASS("%s grazed at %s, gaining energy", Prey.class),
    /** Records a predator eating prey and gaining energy */
    EAT_PREY("%s at %s ate %s at %s, gaining energy", Predator.class, Prey.class),
    /** Records prey fleeing from a predator */
    FLEE("%s at %s is fleeing from %s at %s", Prey.class, Predator.class),
    /** Records predator hunting prey */
    HUNT("%s at %s is hunting %s at %s", Predator.class, Prey.class);

    /** Event description format */
    private final String format;
    /** Event params required for the event */
    private final Class<?>[] params;

    /**
     * Constructs an instance of the EventType.
     *
     * @param format    the event description format string
     * @param params    parameters required for the event
     */
    EventType(String format, Class<?>... params) {
        this.format = format;
        this.params = params;
    }

    /**
     * Generates a formatted description of the event based on its type and provided parameters.
     *
     * @param params the parameters required to populate the event description format;
     *               these should match the expected types for the event
     * @return a string representing the formatted event description
     */
    public String getDescription(Object... params) {
        return switch (this) {
            case SIMULATION_START, SIMULATION_PAUSE, SIMULATION_RESUME, SIMULATION_END, SIMULATION_TURN -> format;

            case DIE_ENERGY, DIE_AGE, SPAWN, MOVE ->
                String.format(format, ((Animal)params[0]).getId(), ((Animal)params[0]).getPosition().toString());

            case DIE_EATEN, FLEE ->
                String.format(format, ((Prey)params[0]).getId(), ((Prey)params[0]).getPosition().toString(), ((Predator)params[1]).getId(), ((Predator)params[1]).getPosition().toString());

            case REPRODUCE ->
                String.format(format, ((Animal)params[0]).getId(), ((Animal)params[0]).getPosition().toString(),
                        ((Animal)params[1]).getId(), ((Animal)params[1]).getPosition().toString());

            case EAT_GRASS ->
                String.format(format, ((Prey)params[0]).getId(), ((Prey)params[0]).getPosition().toString());

            case EAT_PREY, HUNT ->
                String.format(format, ((Predator)params[0]).getId(), ((Predator)params[0]).getPosition().toString(),
                        ((Prey)params[1]).getId(), ((Prey)params[1]).getPosition().toString());

        };
    }

    /**
     * Validates the provided parameters against the expected parameter types
     * defined in the containing class. Ensures the number, order, and types
     * of parameters match the expected configuration.
     *
     * @param givenParams the parameters to validate against the expected types;
     *                    each parameter in this array should be an instance
     *                    of the corresponding type specified in the expected types
     * @throws IllegalArgumentException if the number of parameters does not match
     *                                  the expected number, or if a parameter type
     *                                  does not match the expected type
     */
    public void validate(Object... givenParams) {
        if (givenParams.length != params.length) {
            throw new IllegalArgumentException("Expected " + params.length + " parameters, but got " + givenParams.length);
        }
        for (int i = 0; i < params.length; i++) {
            if (!params[i].isInstance(givenParams[i])) {
                throw new IllegalArgumentException("Parameter " + (i + 1) + " must be of type " + params[i].getSimpleName() + ", but got " + givenParams[i].getClass().getSimpleName());
            }
        }
    }
}
