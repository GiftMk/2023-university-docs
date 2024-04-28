package uc.seng301.cardbattler.asg4.cards;

/**
 * Card type enum of allowed types used for fetching cards from the API in a
 * controlled manner {@link CardGenerator}
 */
public enum CardType {
    /**
     * Monster card types
     */
    MONSTER,
    /**
     * Spell card type
     */
    SPELL,
    /**
     * Trap card type
     */
    TRAP,
    /**
     * Random card type. Will assume no preference and use one of the other types
     */
    RANDOM
}
