package uc.seng301.cardbattler.lab6.cards;

import uc.seng301.cardbattler.lab6.model.Card;

/**
 * Card generation interface
 */
public interface CardGenerator {
    /**
     * Get a random card
     * @return a randomly generated card
     */
    Card getRandomCard();
}
