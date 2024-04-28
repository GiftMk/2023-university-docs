package uc.seng301.cardbattler.lab6.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uc.seng301.cardbattler.lab6.model.Card;
import uc.seng301.cardbattler.lab6.model.CardPosition;

/**
 * Card
 */
public class CardResponse {
    @JsonDeserialize
    @JsonProperty("name")
    private String name;

    @JsonDeserialize
    @JsonProperty("race")
    private String race;


    @JsonDeserialize
    @JsonProperty("atk")
    private int attack;

    @JsonDeserialize
    @JsonProperty("def")
    private int defense;

    @JsonDeserialize
    @JsonProperty("level")
    private int level;

    @JsonDeserialize
    @JsonProperty("type")
    private String type;

    @JsonDeserialize
    @JsonProperty("attribute")
    private String attribute;

    @JsonDeserialize
    @JsonProperty("desc")
    private String description;

    public CardResponse() {
        // no-args jackson constructor
    }

    @Override
    public String toString() {
        return "CardResponse{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", race='" + race + '\'' +
                ", attribute='" + attribute + '\'' +
                ", attack=" + attack +
                ", defense=" + defense +
                ", level=" + level +
                '}';
    }

    public Card toCard() {
        Card card = new Card();
        card.setName(name);
        card.setAttack(attack);
        card.setDefence(defense);
        card.setLife(0);
        card.setCardPosition(CardPosition.ATTACK);
        return card;
    }


}
