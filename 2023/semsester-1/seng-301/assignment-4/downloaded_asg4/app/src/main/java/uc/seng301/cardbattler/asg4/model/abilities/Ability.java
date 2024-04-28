package uc.seng301.cardbattler.asg4.model.abilities;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Default interface for executing one of a Card's abilities
 */
public interface Ability {

    /**
     * Execute the ability
     *
     * @param abilityCard  The card who this ability belongs to
     * @param playState    The current play state
     * @param actor        The card that is currently making an action
     * @param target       The card that is currently being targeted by the actor
     * @param actorIsAlly  true if the actor is an ally card, or false if an enemy
     * @param targetIsAlly true if the target is an ally card, or false if an enemy
     */
    void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly);
}
