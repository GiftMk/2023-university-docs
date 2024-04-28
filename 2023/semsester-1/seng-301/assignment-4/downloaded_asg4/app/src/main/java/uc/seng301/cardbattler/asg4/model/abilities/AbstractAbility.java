package uc.seng301.cardbattler.asg4.model.abilities;

/**
 * Abstract ability modifier
 */
public abstract class AbstractAbility implements Ability {
    /**
     * Ability to modify
     */
    protected Ability ability;

    /**
     * Gets the modified ability
     * 
     * @return the underlying modified ability
     */
    public Ability getAbility() {
        return ability;
    }
}
