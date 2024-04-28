package uc.seng301.cardbattler.asg4.model;

/**
 * Represents the cards position {@link Monster} only (either Attacking or
 * Defending)
 */
public enum CardPosition {
    /**
     * Represents the attacking position
     */
    ATTACK("attacking"),
    /**
     * Represents the defending position
     */
    DEFEND("defending");

    /**
     * Human-readable string representation of enum
     */
    public final String label;

    /**
     * Basic string argument enum constructor
     * 
     * @param label string representation of the enum
     */
    CardPosition(String label) {
        this.label = label;
    }

    /**
     * Get the CardPosition from its label
     * 
     * @param label card position label to look-up
     * @return The matching CardPosition or null if there is no match
     */
    public static CardPosition valueOfLabel(String label) {
        for (CardPosition e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
