package uc.seng301.cardbattler.lab6.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_player")
    private Long playerId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_player")
    private List<Deck> decks;

    private String name;

    public Player() {
        // no-args constructor needed by JPA
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Player (%d): %s%n\tDecks:%n", playerId, name));
        for (Deck deck : decks) {
            sb.append(String.format("\t\tDeck (%d): %s%n\t\t\tCards:%n", deck.getDeckId(), deck.getName()));
            for (Card card : deck.getCards()) {
                sb.append(
                        String.format("\t\t\t\tCard (%d): %s", card.getCardId(), card.getCardDescription()));
            }
        }
        return sb.toString();
    }

    /* Getters and setters omitted */

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }
}
