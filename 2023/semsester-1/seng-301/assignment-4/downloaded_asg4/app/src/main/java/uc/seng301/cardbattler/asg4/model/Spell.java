package uc.seng301.cardbattler.asg4.model;

/**
 * This a special type of {@link Card} to represent spells
 */
public class Spell extends Card {

    /**
     * Basic no-args constructor
     */
    public Spell() {
        // empty for JPA
    }

    /**
     * Copy constructor
     * 
     * @param spell a spell to copy
     */
    public Spell(Spell spell) {
        setName(spell.getName());
        setDescription(spell.getDescription());
    }

    @Override
    public String getCardDescription() {
        return String.format("Spell -- %s", getName());
    }

}
