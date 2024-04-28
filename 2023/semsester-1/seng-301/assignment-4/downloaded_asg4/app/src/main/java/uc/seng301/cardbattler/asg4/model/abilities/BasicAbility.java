package uc.seng301.cardbattler.asg4.model.abilities;

import java.util.function.BiConsumer;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;

/**
 * Basic ability implementation that executes some {@link BiConsumer} of
 * ({@link Card}, {@link Integer}) to fulfil its ability
 */
public class BasicAbility implements Ability {
    /**
     * A BiConsumer represents an operation that takes two parameters in this case
     * Card and int
     * We can then pass some operation such as a method reference which will be
     * called when we call
     * operation.accept(Card, int)
     */
    private final BiConsumer<Card, Integer> operation;
    private final String effect;
    private final int value;

    /**
     * Creates a new basic ability that affects a card
     * 
     * @param operation Operation to execute with the Card (provided during
     *                  execute() and the int value provided)
     * @param effect    a description of the effect of this ability
     * @param value     value to be passed to the operation
     */
    public BasicAbility(BiConsumer<Card, Integer> operation, String effect, int value) {
        this.operation = operation;
        this.effect = effect;
        this.value = value;
    }

    @Override
    public void execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly,
            boolean targetIsAlly) {
        if (abilityCard.getNumNextActionsBlocked() == 0) {
            if (target != null) {
                System.out.println(String.format("%s Executing %s on %s with value %d", abilityCard.getName(),
                        effect, target.getName(), value));
                operation.accept(target, value);
            }
        } else {
            abilityCard.decrementNumNextActionsBlocked();
        }
    }
}
