package uc.seng301.cardbattler.lab6.model;

import jakarta.persistence.*;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_card")
    private Long cardId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_deck")
    private Deck deck;

    private String name;

    private int attack;
    private int defence;
    private int life;
    private CardPosition cardPosition;


    public Card() {
        // a public constructor is needed by JPA
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public void setCardPosition(CardPosition cardPosition) {
        this.cardPosition = cardPosition;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", deck=" + deck +
                ", name='" + name + '\'' +
                ", attack=" + attack +
                ", defence=" + defence +
                ", life=" + life +
                ", currentPosition=" + cardPosition.label+
                '}';
    }

    /**
     * Get human-readable card description to display in terminal
     * @return human-readable card description
     */
    public String getCardDescription() {
        return String.format("%s -- Atk: %d Def: %d Life: %d -- Currently: %s", name, attack, defence, life, cardPosition.label);
    }
}
