package uc.seng301.cardbattler.asg4.game.actions.builders;

import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

/**
 * Concrete builder for building a basic AI action
 */
public class BasicAIBuilder extends ActionBuilder {
    @Override
    public Action build() {
        if (numCardsPlayed == 1) // only play 1 card per turn
            return null;
        if (!allyBoard.getHand().isEmpty()) {
            Card selectedCard = null;
            if (!enemyBoard.getMonsterSlots().isEmpty()) {
                selectedCard = enemyBoard.getMonsterSlots().get(0);
            }
            return new Action(allyBoard.getHand().get(0),
                allyBoard.getHand().get(0) instanceof Trap || allyBoard.getHand().get(0) instanceof Spell ? null
                    : selectedCard);
        }
        return null;
    }
}
