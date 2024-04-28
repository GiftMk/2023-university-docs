package uc.seng301.cardbattler.asg4.game;

import uc.seng301.cardbattler.asg4.cards.CardGenerator;
import uc.seng301.cardbattler.asg4.cards.CardType;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

/**
 * Handle Battle Deck Creation
 * Creates a {@link Deck} with 20 cards exactly, and specific or randomised card
 * type distribution
 */
public class BattleDeckCreator {
    /**
     * Minimum number of {@link Monster}s required in a valid battle deck
     */
    public static final int MIN_NUM_MONSTERS = 5;
    /**
     * Minimum number of {@link Spell}s required in a valid battle deck
     */
    public static final int MIN_NUM_SPELLS = 3;
    /**
     * Minimum number of {@link Trap}s required in a valid battle deck
     */
    public static final int MIN_NUM_TRAPS = 3;
    /**
     * Exact deck size required for a valid battle deck
     */
    public static final int DECK_SIZE = 20;

    private final CardGenerator cardProxy;

    /**
     * Create a BattleDeckGenerator with a specific underlying {@link CardGenerator}
     * to fetch the cards from
     * 
     * @param cardGenerator specific CardGenerator implementation for fetching cards
     */
    public BattleDeckCreator(CardGenerator cardGenerator) {
        cardProxy = cardGenerator;
    }

    /**
     * Populates a deck with the minimum distribution of card types and the rest
     * randomly
     * 
     * @param deck Deck to be populated IN-PLACE
     */
    public void populateRandomBattleDeck(Deck deck) {
        populateRandomBattleDeck(deck, MIN_NUM_MONSTERS, MIN_NUM_SPELLS, MIN_NUM_TRAPS);
    }

    /**
     * Populates a deck with the provided distribution of card types. Add random
     * card types if the sum of parameters is less than 20.
     * 
     * @param deck        Deck to be populated IN-PLACE
     * @param numMonsters number of monsters for the deck
     * @param numSpells   number of spells for the deck
     * @param numTraps    number of traps for the deck
     * @throws IllegalArgumentException if the sum of card types exceed 20 or if any
     *                                  number of cards is negative
     */
    public void populateRandomBattleDeck(Deck deck, int numMonsters, int numSpells, int numTraps) {
        if (numMonsters < 0 || numSpells < 0 || numTraps < 0) {
            throw new IllegalArgumentException("Cannot create a deck with negative numbers of cards");
        }
        if (numMonsters + numSpells + numTraps > 20)
            throw new IllegalArgumentException(String.format("Cannot have more than %d cards", DECK_SIZE));
        for (int i = 0; i < numMonsters; i++) {
            deck.addCards(cardProxy.getRandomCardOfType(CardType.MONSTER));
        }
        for (int i = 0; i < numSpells; i++) {
            deck.addCards(cardProxy.getRandomCardOfType(CardType.SPELL));
        }
        for (int i = 0; i < numTraps; i++) {
            deck.addCards(cardProxy.getRandomCardOfType(CardType.TRAP));
        }
        for (int i = 0; i < DECK_SIZE - numMonsters - numTraps - numSpells; i++) {
            deck.addCards(cardProxy.getRandomCardOfType(CardType.RANDOM));
        }
    }

    /**
     * Check to see if the provided deck is a valid battle deck
     * 
     * @param deck Deck to validate
     * @return true if the deck is valid else false
     */
    public static boolean deckIsValid(Deck deck) {
        return deck != null &&
                deck.getCards().size() == 20 &&
                deck.getCards().stream().filter(Monster.class::isInstance).toList().size() >= MIN_NUM_MONSTERS &&
                deck.getCards().stream().filter(Spell.class::isInstance).toList().size() >= MIN_NUM_SPELLS &&
                deck.getCards().stream().filter(Trap.class::isInstance).toList().size() >= MIN_NUM_TRAPS;
    }
}
