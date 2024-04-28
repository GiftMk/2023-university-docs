package uc.seng301.cardbattler.asg4.cards.builders;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.CardPosition;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.abilities.Ability;
import uc.seng301.cardbattler.asg4.model.abilities.BasicAbility;
import uc.seng301.cardbattler.asg4.model.abilities.CanTargetSelf;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyIfPlayState;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyOnType;
import uc.seng301.cardbattler.asg4.model.abilities.TargetIsAllyOrEnemy;

/**
 * Concrete builder for creating Monster cards
 */
public class MonsterBuilder extends CardBuilder {
    @Override
    public Monster build() {
        Monster monster = new Monster();
        Ability ability = new BasicAbility(Card::damageCard, "attack", attack);
        ability = new TargetIsAllyOrEnemy(ability, false);
        ability = new OnlyIfPlayState(ability, PlayState.ON_PLAY);
        ability = new OnlyOnType(ability, Monster.class);
        ability = new CanTargetSelf(ability, false);
        monster.addAbility(ability);
        monster.setAttack(attack);
        monster.setDefence(defence);
        monster.setLife(0);
        monster.setCardPosition(CardPosition.ATTACK);
        monster.setName(name);
        monster.setDescription(description);
        return monster;
    }
}
