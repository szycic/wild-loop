package org.wildloop;

public enum EventParams {
    /** No additional parameters for the event */
    NONE(0),
    /** World where the worldwide event occurs */
    WORLDWIDE(1, World.class),
    /** Animal involved in the event */
    ANIMAL(2, World.class, Animal.class),
    /** Three animals (two parents and one offspring) involved in the event */
    PARENTS_OFFSPRING(4, World.class, Animal.class, Animal.class, Animal.class),
    /** Predator and prey involved in the event (in that order) */
    PREDATOR_PREY(3, World.class, Predator.class, Prey.class),
    /** Prey and predator involved in the event (in that order) */
    PREY_PREDATOR(3, World.class, Prey.class, Predator.class);

    /** Number of additional parameters required for the event */
    private final int paramCount;
    /** Types of additional parameters required for the event */
    private final Class<?>[] paramTypes;

    EventParams(int paramCount, Class<?>... paramTypes) {
        this.paramCount = paramCount;
        this.paramTypes = paramTypes;
    }

    /**
     * Returns the number of additional parameters required for the event.
     *
     * @return the number of parameters
     */
    public int getParamCount() {
        return paramCount;
    }

    /**
     * Returns the types of additional parameters required for the event.
     *
     * @return an array of Class objects representing the parameter types
     */
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void validate(Object... params) {
        if (params.length != paramCount) {
            throw new IllegalArgumentException("Expected " + paramCount + " parameters, but got " + params.length);
        }
        for (int i = 0; i < paramCount; i++) {
            if (!paramTypes[i].isInstance(params[i])) {
                throw new IllegalArgumentException("Parameter number " + i+1 + " must be of type " + paramTypes[i].getSimpleName() + ", but got " + params[i].getClass().getSimpleName());
            }
        }
    }
}
