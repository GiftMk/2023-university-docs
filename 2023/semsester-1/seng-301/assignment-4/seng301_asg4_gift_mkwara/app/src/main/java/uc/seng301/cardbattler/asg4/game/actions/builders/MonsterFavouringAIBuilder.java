package uc.seng301.cardbattler.asg4.game.actions.builders;

import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Monster;

import java.util.Comparator;
import java.util.List;

/**
 * Concrete builder for building a monster favouring AI action
 */
public class MonsterFavouringAIBuilder extends ActionBuilder {
    @Override
    public Action build() {
        if (numCardsPlayed == 1) // only play 1 card per turn
            return null;
        if (!allyBoard.getHand().isEmpty()) {
            Card cardToPlay;
            List<Card> monstersInHand = allyBoard.getHand().stream().filter(Monster.class::isInstance).toList();
            if (!monstersInHand.isEmpty())
                cardToPlay = monstersInHand.get(0);
            else
                cardToPlay = allyBoard.getHand().get(0);
            
            Card target = null;
            if (cardToPlay instanceof Monster && !enemyBoard.getMonsterSlots().isEmpty()) {
                target = enemyBoard.getMonsterSlots().stream().sorted(Comparator.comparingInt(Monster::getLife))
                    .toList().get(0);
            }
            return new Action(cardToPlay, target);
        }
        return null;
    }
}
