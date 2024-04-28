package uc.seng301.cardbattler.asg4.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */

public class Deck {
    private List<Card> cards;

    private String name;

    /**
     * Default empty constructor
     */
    public Deck() {
        // a (public) constructor is needed by JPA
    }

    /**
     * Copy constructor, the player will be referenced, not deep copied. Cards will
     * be copied (i.e. cloned)
     * 
     * @param deck a deck to copy
     */
    public Deck(Deck deck) {
        setName(deck.getName());
        setCards(new ArrayList<>());
        cards = new ArrayList<>();
        deck.getCards().forEach(card -> {
            if (card instanceof Monster monster) {
                cards.add(new Monster(monster));
            } else if (card instanceof Spell spell) {
                cards.add(new Spell(spell));
            } else {
                cards.add(new Trap((Trap) card));
            }
        });
    }

    /**
     * Get name of deck
     * 
     * @return name of deck
     */
    public String getName() {
        return name;
    }

    /**
     * Set deck name
     * 
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get a list of all cards that make up the deck
     * Should not be modified through a getter {@see Deck.addCards} to add cards to
     * the deck
     * 
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Set the list of cards that make up the deck
     * 
     * @param cards list of cards to set
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Adds any number of cards to the deck
     * 
     * @param cards cards to add
     */
    public void addCards(Card... cards) {
        this.cards.addAll(Arrays.asList(cards));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Deck (" + getName() + ")" + ", with cards:\n");
        getCards().forEach(card -> builder.append("\t").append(card.toString()).append("\n"));
        return builder.toString();
    }
}
