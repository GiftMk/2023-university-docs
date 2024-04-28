package uc.seng301.cardbattler.asg4.model.abilities;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability to make it only hit itself, or only hit others
 */
public class CanTargetSelf extends AbstractAbility {
    private final boolean canTriggerOnSelf;

    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability          ability to modify
     * @param canTriggerOnSelf true if only trigger on self, false if only trigger
     *                         on others
     */
    public CanTargetSelf(Ability ability, boolean canTriggerOnSelf) {
        this.ability = ability;
        this.canTriggerOnSelf = canTriggerOnSelf;
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        if (canTriggerOnSelf == abilityCard.equals(target)) {
            ability.execute(abilityCard, playState, actor, target, actorIsAlly, targetIsAlly);
        }
    }
}
