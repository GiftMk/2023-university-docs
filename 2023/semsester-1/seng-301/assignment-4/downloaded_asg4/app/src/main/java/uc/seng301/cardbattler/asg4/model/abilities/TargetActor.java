package uc.seng301.cardbattler.asg4.model.abilities;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability by swapping the actor and the target
 */
public class TargetActor extends AbstractAbility {
    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability ability to modify
     */
    public TargetActor(Ability ability) {
        this.ability = ability;
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        ability.execute(abilityCard, playState, target, actor, targetIsAlly, actorIsAlly);
    }
}
