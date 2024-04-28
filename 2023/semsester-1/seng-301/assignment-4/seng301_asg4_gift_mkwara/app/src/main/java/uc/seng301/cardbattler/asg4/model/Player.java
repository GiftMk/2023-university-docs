package uc.seng301.cardbattler.asg4.model;

import uc.seng301.cardbattler.asg4.game.PlayerAIOperation;

/**
 *
 */
public class Player {
    private Deck deck;
    private String name;
    private PlayerAIOperation playerAI;

    /**
     * Get player's name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets player's name
     * 
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets player's AI operation (play style)
     * Note this is a method reference that complies with {@link PlayerAIOperation}
     * 
     * @return player AI operation (play style)
     */
    public PlayerAIOperation getPlayerAI() {
        return playerAI;
    }

    /**
     * Sets player's AI operation (play style)
     * Note this is a method reference that complies with {@link PlayerAIOperation}
     * 
     * @param playerAI player AI operation (play style) to set
     */
    public void setPlayerAI(PlayerAIOperation playerAI) {
        this.playerAI = playerAI;
    }

    /**
     * Gets player's deck
     * 
     * @return player's deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets player's deck
     * 
     * @param deck deck to set
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Player (%s), play style (%s),  ", name, playerAI));
        if (deck == null) {
            sb.append("No deck yet");
        } else {
            sb.append("Deck\n");
            sb.append("  ").append(deck);
        }
        return sb.toString();
    }
}
