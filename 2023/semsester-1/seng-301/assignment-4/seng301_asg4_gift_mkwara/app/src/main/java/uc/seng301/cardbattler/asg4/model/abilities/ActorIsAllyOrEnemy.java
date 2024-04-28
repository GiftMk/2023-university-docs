package uc.seng301.cardbattler.asg4.model.abilities;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability, do it can be triggered only if the actor is an ally or and
 * enemy
 */
public class ActorIsAllyOrEnemy extends AbstractAbility {
    private final boolean isAlly;

    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability ability to modify
     * @param isAlly  true to execute if actor is ally or false if actor is enemy
     */
    public ActorIsAllyOrEnemy(Ability ability, boolean isAlly) {
        this.ability = ability;
        this.isAlly = isAlly;
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        if (this.isAlly == actorIsAlly) {
            ability.execute(abilityCard, playState, target, actor, actorIsAlly, targetIsAlly);
        }
    }
}
