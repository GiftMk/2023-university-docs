package uc.seng301.cardbattler.asg4.accessor;

import java.util.List;

import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Player;

/**
 * This class offers helper methods for creating {@link Deck}s
 *
 */
public class DeckAccessor {

    /**
     * Create a {@link Deck} object with the given parameters and set it as the
     * provided players deck
     * Note that if the player already has a deck it will be over-written
     *
     * @param name   The Deck name must be [a-zA-Z0-9 ] (not null, empty, or only
     *               numerics)
     * @param player The Player whose deck it is (cannot be null)
     * @param cards  The cards to be in the deck, must not be null and must have at
     *               least 1 card
     * @return The Deck object with given parameters
     * @throws IllegalArgumentException If any of the above preconditions for input
     *                                  arguments are violated
     */
    public Deck createDeck(String name, Player player, List<Card> cards) throws IllegalArgumentException {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Deck name cannot be empty.");
        }
        if (name.matches("\\d+") || !name.matches("\\w+")) {
            throw new IllegalArgumentException("Deck name be alphanumerical but cannot only be numeric");
        }
        if (player == null) {
            throw new IllegalArgumentException("Deck must be associated with a player");
        }
        if (cards == null) {
            throw new IllegalArgumentException("Deck must not be null");
        }
        Deck deck = new Deck();
        deck.setName(name);
        deck.setCards(cards);
        player.setDeck(deck);
        return deck;
    }
}
