package uc.seng301.cardbattler.lab4.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_player")
    private long playerId;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_player")
    private List<Deck> decks;

    public Player() {}

    public Player(String name, List<Deck> decks) {
        this.name = name;
        this.decks = decks;
    }

    public long getPlayerId() {
        return playerId;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Player (%d): %s%n\tDecks:%n".formatted(playerId, name));
        decks.forEach(deck -> {
            builder.append(
                "\t\tDeck (%d): %s%n\t\t\tCards:%n".formatted(deck.getDeckId(), deck.getName())
            );
            deck.getCards().forEach(card -> builder.append(
                "\t\t\t\tCard (%d): %s -- Attack: %d, Defence: %d, Life: %d%n".formatted(
                    card.getCardId(),
                    card.getName(),
                    card.getAttack(),
                    card.getDefence(),
                    card.getLife()
                )
            ));
        });
        return builder.toString();
    }
}
