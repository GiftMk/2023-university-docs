package uc.seng301.cardbattler.asg4.model;

import java.util.Objects;

/**
 * This is a special type of {@link Card} to represent Monsters
 * Notably monsters have an attack, defence, and life value
 */
public class Monster extends Card {
    private int attack;
    private int defence;
    private int life;
    private CardPosition cardPosition;

    /**
     * Default no-args constructor
     */
    public Monster() {
    }

    /**
     * Copy constructor
     * 
     * @param monster a monster to copy
     */
    public Monster(Monster monster) {
        setName(monster.getName());
        setDescription(monster.getDescription());
        setCardPosition(monster.getCardPosition());
        setAttack(monster.getAttack());
        setDefence(monster.getDefence());
        setLife(monster.getLife());
    }

    /**
     * Get monster's attack
     * 
     * @return attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Set monster's attack
     * 
     * @param attack attack value to set
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Get monster's defence
     * 
     * @return defence value
     */
    public int getDefence() {
        return defence;
    }

    /**
     * Set monster's defence
     * 
     * @param defence defence value to set
     */
    public void setDefence(int defence) {
        this.defence = defence;
    }

    /**
     * Get monster's current life
     * 
     * @return life value
     */
    public int getLife() {
        return life;
    }

    /**
     * Set monster's life value
     * 
     * @param life life value to set
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Get monster's current card position
     * 
     * @return current card position
     */
    public CardPosition getCardPosition() {
        return cardPosition;
    }

    /**
     * Set monster's card position
     * 
     * @param cardPosition card position to set
     */
    public void setCardPosition(CardPosition cardPosition) {
        this.cardPosition = cardPosition;
    }

    @Override
    public String getCardDescription() {
        return String.format("Monster -- %s -- Atk: %d Def: %d Life: %d -- Currently: %s", getName(), attack, defence,
                life,
                cardPosition.label);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Monster)) {
            return false;
        }
        Monster monster = (Monster) o;
        return attack == monster.attack && defence == monster.defence && life == monster.life
                && Objects.equals(cardPosition, monster.cardPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attack, defence, life, cardPosition);
    }

    @Override
    public void damage(int damageAmount) {
        this.life -= damageAmount;
    }

    @Override
    public void heal(int healAmount) {
        this.life += healAmount;
    }

}
