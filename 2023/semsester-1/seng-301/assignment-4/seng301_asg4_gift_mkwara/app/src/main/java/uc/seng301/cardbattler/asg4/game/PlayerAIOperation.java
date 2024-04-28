package uc.seng301.cardbattler.asg4.game;

import uc.seng301.cardbattler.asg4.game.actions.Action;

/**
 * A simple interface which allows for the passing of PlayStyle method
 * references
 */
public interface PlayerAIOperation {
    /**
     * Play style functionality is defined through the following method
     * 
     * @param allyBoard      current player's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played so far in the turn
     * @return The action the play style chooses to execute
     */
    Action execute(Board allyBoard, Board enemyBoard, int numCardsPlayed);
}
