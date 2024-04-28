package uc.seng301.cardbattler.asg4.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import uc.seng301.cardbattler.asg4.cards.builders.CardBuilder;
import uc.seng301.cardbattler.asg4.cards.builders.MonsterBuilder;
import uc.seng301.cardbattler.asg4.cards.builders.SpellBuilder;
import uc.seng301.cardbattler.asg4.cards.builders.TrapBuilder;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * {@link Card} API response JSON deserializer (Jackson)
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
    private int defence;

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

    /**
     * No-args Jackson constructor
     */
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
                ", defence=" + defence +
                ", level=" + level +
                '}';
    }

    /**
     * Converts itself to a Card including assigning a default ability for each card
     * type
     * 
     * @return Card representation of json deserialized response
     */
    public Card toCard() {
        CardBuilder cardBuilder;
        
        if (type.toLowerCase().contains("monster")) {
            cardBuilder = new MonsterBuilder();
        } else if (type.toLowerCase().contains("spell")) {
            cardBuilder = new SpellBuilder();
        } else if (type.toLowerCase().contains("trap")) {
            cardBuilder = new TrapBuilder();
        } else {
            return null;
        }
        
        return cardBuilder
            .setName(name)
            .setDescription(description)
            .setAttack(attack)
            .setDefence(defence)
            .build();
    }

}
