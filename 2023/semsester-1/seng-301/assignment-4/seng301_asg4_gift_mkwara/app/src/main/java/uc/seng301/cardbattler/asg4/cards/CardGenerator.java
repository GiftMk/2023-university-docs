package uc.seng301.cardbattler.asg4.cards;

import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Card generation interface
 */
public interface CardGenerator {
    /**
     * Get a random card
     * 
     * @return a randomly generated card
     */
    Card getRandomCard();

    /**
     * Gets a random card of a specific type
     * 
     * @param cardType card type to fetch
     * @return a randomly selected card of the specified type
     */
    Card getRandomCardOfType(CardType cardType);
}
