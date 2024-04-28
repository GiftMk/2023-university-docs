package uc.seng301.cardbattler.asg4.cards.builders;

import uc.seng301.cardbattler.asg4.game.PlayState;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.abilities.Ability;
import uc.seng301.cardbattler.asg4.model.abilities.ActorIsAllyOrEnemy;
import uc.seng301.cardbattler.asg4.model.abilities.BasicAbility;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyIfPlayState;
import uc.seng301.cardbattler.asg4.model.abilities.OnlyOnType;
import uc.seng301.cardbattler.asg4.model.abilities.TargetIsAllyOrEnemy;
import uc.seng301.cardbattler.asg4.model.abilities.TotalTimes;

/**
 * Concrete builder for creating Spell cards
 */
public class SpellBuilder extends CardBuilder {
    @Override
    public Spell build() {
        Spell spell = new Spell();
        Ability ability = new BasicAbility(Card::healCard, "heal", 1000);
        ability = new TotalTimes(ability, 1);
        ability = new ActorIsAllyOrEnemy(ability, true);
        ability = new TargetIsAllyOrEnemy(ability, false);
        ability = new OnlyIfPlayState(ability, PlayState.AFTER_PLAY);
        ability = new OnlyOnType(ability, Monster.class);
        spell.addAbility(ability);
        spell.setName(name);
        spell.setDescription(description);
        return spell;
    }
}