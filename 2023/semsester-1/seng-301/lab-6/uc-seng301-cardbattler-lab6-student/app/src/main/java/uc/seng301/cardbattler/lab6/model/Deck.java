package uc.seng301.cardbattler.lab6.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "deck")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_deck")
    private Long deckId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_player")
    private Player player;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck")
    private List<Card> cards;

    private String name;

    public Deck() {
        // a (public) constructor is needed by JPA
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Adds any number of cards to the deck
     * @param cards cards to add
     */
    public void addCards(Card... cards) {
        this.cards.addAll(Arrays.asList(cards));
    }
}
