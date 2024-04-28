package uc.seng301.cardbattler.asg4.game.actions.builders;

import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.game.Board;

/**
 * Abstract class for building an Action
 */
public abstract class ActionBuilder {
    /**
     * Number of turns a setup favouring AI will skip if an optimal (non-monster
     * card) is not available to play
     */
    protected int turnsToSetup = 5;
    /**
     * Number of turns a reckless AI will skip at the start of the game
     */
    protected int turnsToWait = 5;
    protected Board allyBoard;
    protected Board enemyBoard;
    protected int numCardsPlayed;
    
    public ActionBuilder setAllyBoard(Board allyBoard) {
        this.allyBoard = allyBoard;
        return this;
    }
    
    public ActionBuilder setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
        return this;
    }
    
    public ActionBuilder setNumCardsPlayed(int numCardsPlayed) {
        this.numCardsPlayed = numCardsPlayed;
        return this;
    }
    
    /**
     * Builds the action using the set parameters
     * @return the built action
     */
    public abstract Action build();
}
