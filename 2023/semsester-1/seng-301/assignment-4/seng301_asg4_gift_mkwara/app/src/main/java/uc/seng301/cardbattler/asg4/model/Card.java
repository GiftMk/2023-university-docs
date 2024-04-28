package uc.seng301.cardbattler.asg4.model;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.abilities.Ability;
import uc.seng301.cardbattler.asg4.model.abilities.AbstractAbility;
import uc.seng301.cardbattler.asg4.model.abilities.TotalTimes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Card implements Cloneable {
    private List<Ability> abilities = new ArrayList<>();
    private int numNextActionsBlocked = 0;
    private String name;
    private String description;

    /**
     * Default no-args constructor to specify visibility
     */
    protected Card() {

    }

    /**
     * Get card name
     * @return card name
     */
    public String getName() {
        return name;
    }

    /**
     * Set card name
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get card description
     * @return card description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set card description
     * @param description description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", description=" + getCardDescription() +
                '}';
    }

    /**
     * Get human-readable card description to display in terminal
     *
     * @return human-readable card description
     */
    public abstract String getCardDescription();

    /**
     * Inform the card of the current action, so they can react
     *
     * @param playState Current play state
     * @param actor     acting card
     * @param target    target card
     * @param actorIsAlly true if actor is ally, else false
     * @param targetIsAlly true if target is ally, else false
     * @return false if and only if card is considered dead and needs to be removed from the board
     */
    public boolean reactToAction(PlayState playState, Card actor, Card target, boolean actorIsAlly, boolean targetIsAlly) {
        for (Ability ability : abilities) {
            ability.execute(this, playState, actor, target, actorIsAlly, targetIsAlly);
        }
        if (playState.equals(PlayState.AFTER_PLAY)) {
            if (this instanceof Monster monster) {
                // remove monsters that are 'dead' (life <= 0)
                return monster.getLife() > 0;
            } else {
                // remove Spells or Traps that are 'used' up
                return abilities.stream().filter(d -> {
                    while (d instanceof AbstractAbility a) {
                        if (a instanceof TotalTimes t && t.getNumberOfTimesToTrigger() == 0) {
                            return true;
                        }
                        d = a.getAbility();
                    }
                    return false;
                }).toList().size() != abilities.size();
            }
        }
        return true; // default value; card not to be removed
    }

    /**
     * Card types that have health must implement this method and take damage
     *
     * @param damageAmount damage to take
     */
    public void damage(int damageAmount) {
        // default behaviour ignore
    }

    /**
     * Card types that have health damage must implement this method and heal
     *
     * @param healAmount amount to heal
     */
    public void heal(int healAmount) {
        // default behaviour ignore
    }

    /**
     * Blocks the next specified number of actions
     *
     * @param numNextActionsBlocked the number of future actions to block
     */
    public void block(int numNextActionsBlocked) {
        this.numNextActionsBlocked = numNextActionsBlocked;
    }

    /**
     * Adds an ability to the card
     *
     * @param ability ability to be added
     */
    public void addAbility(Ability ability) {
        this.abilities.add(ability);
    }

    /**
     * Clone implementation for card
     * @return new "deep" copy of card
     */
    @Override
    public Card clone() {
        try {
            Card clone = (Card) super.clone();
            clone.abilities = new ArrayList<>(this.abilities);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Get the number of future actions blocked, if this number is not 0 no actions should be done
     * @return number of future actions blocked
     */
    public int getNumNextActionsBlocked() {
        return numNextActionsBlocked;
    }

    /**
     * Decrement the number of actions blocked
     * Should be used once an action is attempted but blocked
     */
    public void decrementNumNextActionsBlocked() {
        numNextActionsBlocked -= 1;
    }

    /**
     * Deals the specified amount of damage to the card
     *
     * @param card         card ot damage (underlying card.damage() function used)
     * @param damageAmount amount of damage to deal
     */
    public static void damageCard(Card card, int damageAmount) {
        card.damage(damageAmount);
    }

    /**
     * Heals the specified amount of life of the card
     *
     * @param card       card ot damage (underlying card.heal() function used)
     * @param healAmount amount of life to heal
     */
    public static void healCard(Card card, int healAmount) {
        card.heal(healAmount);
    }

    /**
     * Blocks the specified number of future actions of the card
     *
     * @param card       card to block (underlying card.block() function used)
     * @param numToBlock number of actions to block
     */
    public static void blockNextAbilities(Card card, int numToBlock) {
        card.block(numToBlock);
    }
}
