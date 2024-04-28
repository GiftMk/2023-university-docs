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
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_deck")
    private long deckId;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck")
    private List<Card> cards;

    public Deck() {}

    public Deck(String name, List<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public long getDeckId() {
        return deckId;
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
}
