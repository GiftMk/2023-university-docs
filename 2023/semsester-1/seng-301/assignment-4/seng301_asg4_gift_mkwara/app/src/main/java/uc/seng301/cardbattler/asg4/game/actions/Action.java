package uc.seng301.cardbattler.asg4.game.actions;

import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Simple class representing an action between two cards needed for player moves
 */
public class Action {
    private final Card actor;
    private final Card target;

    /**
     * Create a new Action representation
     * 
     * @param actor  Card making the action
     * @param target Card being targeted by the actor
     */
    public Action(Card actor, Card target) {
        this.actor = actor;
        this.target = target;
    }

    /**
     * Get the acting card
     * 
     * @return the acting card
     */
    public Card getActor() {
        return actor;
    }

    /**
     * Get the card targeted by the actor
     * 
     * @return the targeted card
     */
    public Card getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "actor='" + actor.getName() + "' target="
                + (target != null ? "'" + target.getName() + "'" : "no target");
    }
}
