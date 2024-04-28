package uc.seng301.cardbattler.asg4.model.abilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Modify an ability, so it can only be triggered a set number of times total
 */
public class TotalTimes extends AbstractAbility {
    private static final Logger LOGGER = LogManager.getLogger(TotalTimes.class);
    private int numberOfTimesToTrigger;

    /**
     * Basic constructor with required values for initialisation
     *
     * @param ability                ability to modify
     * @param numberOfTimesToTrigger number of times to trigger
     */
    public TotalTimes(Ability ability, int numberOfTimesToTrigger) {
        this.ability = ability;
        this.numberOfTimesToTrigger = numberOfTimesToTrigger;
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        if (this.numberOfTimesToTrigger > 0) {
            numberOfTimesToTrigger -= 1;
            ability.execute(abilityCard, playState, target, actor, actorIsAlly, targetIsAlly);
        } else {
            LOGGER.warn("[WARN] {} Attempting to trigger on {} must be removed this turn", abilityCard.getName(),
                    target.getName());
        }
    }

    /**
     * Get the remaining number of times the ability can trigger.
     * Once this reaches 0 the ability will no longer execute.
     * Any card where all abilities are 'finished' is no longer worth keeping in
     * batter
     *
     * @return number of remaining times the ability can trigger
     */
    public int getNumberOfTimesToTrigger() {
        return numberOfTimesToTrigger;
    }
}
