package uc.seng301.cardbattler.asg4.cards.builders;

import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Abstract class for building a Card
 */
public abstract class CardBuilder {
    protected String name;
    protected String description;
    protected int attack;
    protected int defence;
    
    public CardBuilder setName(String name) {
        this.name = name;
        return this;
    }
    
    public CardBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public CardBuilder setAttack(int attack) {
        this.attack = attack;
        return this;
    }
    
    public CardBuilder setDefence(int defence) {
        this.defence = defence;
        return this;
    }
    
    /**
     * Builds the card using the set parameters
     * @return the built card
     */
    public abstract Card build();
}
