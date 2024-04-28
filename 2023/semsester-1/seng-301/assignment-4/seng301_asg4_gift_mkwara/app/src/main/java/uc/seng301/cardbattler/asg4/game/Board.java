package uc.seng301.cardbattler.asg4.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.CardPosition;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

/**
 * Represents 1 side of the game board of a Yu-Gi-Oh!-like game
 * Stores the deck, hand, and different board card slots
 */
public class Board {
    private final List<Monster> monsterSlots;
    private final List<Spell> spellSlots;
    private final List<Trap> trapSlots;
    private final List<Card> deck = new ArrayList<>();
    private final List<Card> hand = new ArrayList<>();

    /**
     * Create a new GameBoard with the given deck
     *
     * @param deck Deck to be used for a game, the cards of this deck will be copied
     */
    public Board(Deck deck) {
        monsterSlots = new ArrayList<>();
        spellSlots = new ArrayList<>();
        trapSlots = new ArrayList<>();
        this.deck.addAll(deck.getCards().stream().map(Card::clone).toList());
        Collections.shuffle(this.deck);
    }

    /**
     * Plays a card from your hand (The card requested must exist in the current
     * hand
     *
     * @param card Card to play (must exist in hand)
     */
    public void playCard(Card card) {
        if (!hand.remove(card)) {
            throw new GameRuntimeException("Card played was not found in hand");
        }
        if (card instanceof Monster monster) {
            if (CardPosition.ATTACK.equals(monster.getCardPosition())) {
                monster.setLife(monster.getAttack());
            } else {
                monster.setLife(monster.getDefence());
            }
            monsterSlots.add(monster);
        } else if (card instanceof Spell spell) {
            spellSlots.add(spell);
        } else if (card instanceof Trap trap) {
            trapSlots.add(trap);
        }

    }

    /**
     * Draw a specified amount of cards from the deck to the hand
     * Stops drawing once the deck is empty
     *
     * @param amount number of cards to draw
     */
    public void draw(int amount) {
        int drawn = 0;
        while (drawn < amount && !deck.isEmpty()) {
            hand.add(deck.remove(0));
            drawn++;
        }
    }

    /**
     * Gets the current hand
     *
     * @return Cards in current hand
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Gets all the monsters on the board
     *
     * @return monsters on the board
     */
    public List<Monster> getMonsterSlots() {
        return monsterSlots;
    }

    /**
     * Gets all the spells on the board
     *
     * @return spells on the board
     */
    public List<Spell> getSpellSlots() {
        return spellSlots;
    }

    /**
     * Gets all the traps on the board
     *
     * @return traps on the board
     */
    public List<Trap> getTrapSlots() {
        return trapSlots;
    }

    /**
     * Returns a string representation of the cards in the board with the index
     * provided
     *
     * @param idx starting index
     * @return terminal-safe String representation of board
     */
    public String getDisplayableSlots(int idx) {
        StringBuilder sb = new StringBuilder();
        sb.append("Monsters:");
        for (Card c : monsterSlots) {
            sb.append(String.format("%n    [%d]", idx)).append(c.getCardDescription());
            idx++;
        }

        sb.append("\nSpells:");
        for (Card c : spellSlots) {
            sb.append(String.format("%n    [%d]", idx)).append(c.getCardDescription());
            idx++;
        }
        sb.append("\nTraps:");
        for (Card c : trapSlots) {
            sb.append(String.format("%n    [%d]", idx)).append(c.getCardDescription());
            idx++;
        }
        return sb.toString();
    }

    /**
     * Get the current deck
     *
     * @return the board's deck
     */
    public List<Card> getDeck() {
        return deck;
    }

    /**
     * Returns true if the cards are considered allies (i.e. belong to the same
     * player's board)
     *
     * @param c1 card 1
     * @param c2 card 2
     * @return true if the cards are considered allies
     */
    public boolean cardsAreAllies(Card c1, Card c2) {
        // if cards are allies then both of the following criteria must be met,
        // that is c1 exists on the board and c2 exists on the board
        return 2 == ((monsterSlots.contains(c1) || spellSlots.contains(c1) || trapSlots.contains(c1) ? 1 : 0) +
                (monsterSlots.contains(c2) || spellSlots.contains(c2) || trapSlots.contains(c2) ? 1 : 0));
    }
}
