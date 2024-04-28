package uc.seng301.cardbattler.asg4.game.actions.builders;

import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete builder for building a reckless AI action
 */
public class RecklessAIBuilder extends ActionBuilder {
    @Override
    public Action build() {
        if (turnsToWait > 0) {
            turnsToWait--;
            return null;
        }
        if (!allyBoard.getHand().isEmpty()) {
            List<Card> cardsInTypeOrder = new ArrayList<>();
            cardsInTypeOrder.addAll(allyBoard.getHand().stream().filter(Spell.class::isInstance).toList());
            cardsInTypeOrder.addAll(allyBoard.getHand().stream().filter(Monster.class::isInstance).toList());
            cardsInTypeOrder.addAll(allyBoard.getHand().stream().filter(Trap.class::isInstance).toList());
            Card cardToPlay = cardsInTypeOrder.get(0);
            Card target = null;
            if (cardToPlay instanceof Monster && !enemyBoard.getMonsterSlots().isEmpty()) {
                target = enemyBoard.getMonsterSlots().stream()
                    .sorted(Comparator.comparingInt(Monster::getLife).reversed()).toList().get(0);
            }
            return new Action(cardToPlay, target);
        }
        return null;
    }
}
