package uc.seng301.cardbattler.asg4.cards.builders;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;
import uc.seng301.cardbattler.asg4.model.abilities.Ability;
import uc.seng301.cardbattler.asg4.model.abilities.ActorIsAllyOrEnemy;
import uc.seng301.cardbattler.asg4.model.abilities.BasicAbility;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyIfPlayState;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyOnType;
import uc.seng301.cardbattler.asg4.model.abilities.TargetActor;
import uc.seng301.cardbattler.asg4.model.abilities.TotalTimes;

/**
 * Concrete builder for creating Trap cards
 */
public class TrapBuilder extends CardBuilder {
    @Override
    public Trap build() {
        Trap trap = new Trap();
        Ability ability = new BasicAbility(Card::blockNextAbilities, "delay", 1);
        ability = new TargetActor(ability);
        ability = new TotalTimes(ability, 1);
        ability = new ActorIsAllyOrEnemy(ability, false);
        ability = new OnlyOnType(ability, Spell.class, Monster.class);
        ability = new TargetActor(ability);
        ability = new OnlyIfPlayState(ability, PlayState.BEFORE_PLAY);
        trap.addAbility(ability);
        trap.setName(name);
        trap.setDescription(description);
        return trap;
    }
}
