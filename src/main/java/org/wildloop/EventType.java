package org.wildloop;

public enum EventType {
    /** Signals the start of the simulation */
    SIMULATION_START("Simulation started (%s)", EventAdditionalParams.NONE),
    /** Signals pausing of the simulation */
    SIMULATION_PAUSE("Simulation paused (%s)", EventAdditionalParams.NONE),
    /** Signals resuming of the simulation */
    SIMULATION_RESUME("Simulation resumed (%s)", EventAdditionalParams.NONE),
    /** Signals the end of the simulation */
    SIMULATION_END("Simulation ended (%s)", EventAdditionalParams.NONE),
    /** Indicates completion of a simulation turn with turn number */
    SIMULATION_TURN("Turn %s completed (%s)", EventAdditionalParams.NONE),

    /** Records birth/spawn of a new animal */
    SPAWN("%s spawned", EventAdditionalParams.ANIMAL),
    /** Records death of an animal after being eaten by another */
    DIE_EATEN("%s died after being eaten by %s", EventAdditionalParams.PREY_PREDATOR),
    /** Records death of an animal due to energy depletion */
    DIE_ENERGY("%s died due to energy depletion", EventAdditionalParams.ANIMAL),
    /** Records death of an animal due to old age */
    DIE_AGE("%s died of old age", EventAdditionalParams.ANIMAL),

    /** Records movement of an animal to a new position */
    MOVE("%s moved to %s", EventAdditionalParams.ANIMAL),
    /** Records reproduction between two animals creating offspring */
    REPRODUCE("%s at %s reproduced with %s at %s, creating offspring %s at %s", EventAdditionalParams.PARENTS_OFFSPRING),
    /** Records a prey animal grazing and gaining energy */
    EAT_GRASS("%s grazed at %s, gaining energy", EventAdditionalParams.ANIMAL),
    /** Records a predator eating prey and gaining energy */
    EAT_PREY("%s at %s ate %s at %s, gaining energy", EventAdditionalParams.PREDATOR_PREY),
    /** Records prey fleeing from a predator */
    FLEE("%s at %s is fleeing from %s at %s", EventAdditionalParams.PREY_PREDATOR),
    /** Records predator hunting prey */
    HUNT("%s at %s is hunting %s at %s", EventAdditionalParams.PREDATOR_PREY);

    /** Event description format */
    private final String format;
    /** Event params required for the event description */
    private final EventAdditionalParams additionalParams;

    /**
     * Constructs an instance of the EventType.
     *
     * @param format            the event description format string
     * @param additionalParams  additional parameters required for the event
     */
    EventType(String format, EventAdditionalParams additionalParams) {
        this.format = format;
        this.additionalParams = additionalParams;
    }

    /**
     * Generates a formatted description of an event based on the event type and provided arguments.
     * The format and required number of arguments are predetermined for each event type.
     *
     * @param params variable number of arguments required to format the event description.
     *               The number and type of arguments must match the event type's format.
     * @return the formatted event description string.
     * @throws IllegalArgumentException if the number of arguments provided does not match
     *                                  the required number of arguments for the event type.
     */
    public String getDescription(World world, Object... params) {
        if (params.length != this.additionalParams.getParamCount()) {
            throw new IllegalArgumentException("Event type " + this.name() + " requires " + this.additionalParams.getParamCount() + " arguments for constructing description, but " + params.length + " were provided");
        }
        return String.format(format, params);
    }

    /**
     * Retrieves the parameter type associated with the event.
     *
     * @return the parameter type of the event as an {@link EventAdditionalParams} instance.
     */
    public EventAdditionalParams getAdditionalParams() {
        return additionalParams;
    }
}
