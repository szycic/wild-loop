package org.wildloop;

public enum EventAdditionalParams {
    /** No additional parameters for the event */
    NONE(0),
    /** Animal involved in the event */
    ANIMAL(1),
    /** Three animals (two parents and one offspring) involved in the event */
    PARENTS_OFFSPRING(3),
    /** Predator and prey involved in the event (in that order) */
    PREDATOR_PREY(2),
    /** Prey and predator involved in the event (in that order) */
    PREY_PREDATOR(2);

    private final int paramCount;

    EventAdditionalParams(int paramCount) {
        this.paramCount = paramCount;
    }

    public int getParamCount() {
        return paramCount;
    }
}
