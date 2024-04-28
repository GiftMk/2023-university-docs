package uc.seng301.cardbattler.asg4.model.abilities;

import java.util.ArrayList;
import java.util.List;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability, so it only happens when a specific type of card is the
 * target
 */
public class OnlyOnType extends AbstractAbility {
    List<Class<? extends Card>> classes = new ArrayList<>();

    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability ability to modify
     * @param types   Card types to execute on
     */
    @SafeVarargs
    public OnlyOnType(Ability ability, Class<? extends Card>... types) {
        this.ability = ability;
        this.classes.addAll(List.of(types));
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        for (Class<? extends Card> c : classes) {
            if (target != null && c.isAssignableFrom(target.getClass())) {
                ability.execute(abilityCard, playState, actor, target, actorIsAlly, targetIsAlly);
                return;
            }
        }
    }
}
