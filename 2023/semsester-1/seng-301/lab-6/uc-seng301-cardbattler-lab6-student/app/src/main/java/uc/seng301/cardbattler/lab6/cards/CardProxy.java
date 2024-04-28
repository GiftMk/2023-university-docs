package uc.seng301.cardbattler.lab6.cards;

import uc.seng301.cardbattler.lab6.model.Card;

/**
 * Card generation proxy for getting random card from API
 */
public class CardProxy implements CardGenerator {
    private final CardService cardService;

    /**
     * Create a new Card proxy using the {@link CardService} implementation
     */
    public CardProxy() {
        this.cardService = new CardService();
    }

    @Override
    public Card getRandomCard() {
        return cardService.getRandomCard();
    }
}
