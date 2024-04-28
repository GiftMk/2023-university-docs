package uc.seng301.cardbattler.asg4.model;

/**
 * This is a special type of {@link Card} to represent Traps
 */
public class Trap extends Card {

    /**
     * Basic no-args constructor
     */
    public Trap() {
        // JPA
    }

    /**
     * Copy constructor
     * 
     * @param trap a trap to copy
     */
    public Trap(Trap trap) {
        setName(trap.getName());
        setDescription(trap.getDescription());
    }

    @Override
    public String getCardDescription() {
        return String.format("Trap -- %s", getName());
    }
}
