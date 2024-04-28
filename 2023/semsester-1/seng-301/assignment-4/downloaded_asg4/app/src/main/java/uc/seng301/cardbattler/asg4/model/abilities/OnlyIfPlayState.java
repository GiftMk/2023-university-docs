package uc.seng301.cardbattler.asg4.model.abilities;

import java.util.ArrayList;
import java.util.List;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability so it only happens on specific {@link PlayState}s
 */
public class OnlyIfPlayState extends AbstractAbility {
    private final List<PlayState> playStates = new ArrayList<>();

    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability    ability to modify
     * @param playStates play states to execute on
     */
    public OnlyIfPlayState(Ability ability, PlayState... playStates) {
        this.ability = ability;
        this.playStates.addAll(List.of(playStates));
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        if (playStates.contains(playState)) {
            ability.execute(abilityCard, playState, target, actor, actorIsAlly, targetIsAlly);
        }
    }
}
