package uc.seng301.cardbattler.asg4.game;

import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.game.actions.builders.BasicAIBuilder;
import uc.seng301.cardbattler.asg4.game.actions.builders.MonsterFavouringAIBuilder;
import uc.seng301.cardbattler.asg4.game.actions.builders.ActionBuilder;
import uc.seng301.cardbattler.asg4.game.actions.builders.RecklessAIBuilder;
import uc.seng301.cardbattler.asg4.game.actions.builders.SetupFavouringAIBuilder;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Player;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

/**
 * A class that includes several different play styles as methods.
 * These static methods are used as method references to represent a
 * {@link Player}s AI
 */
public class PlayStyles {

    /**
     * Default private constructor to prevent instantiation of static-only class
     */
    private PlayStyles() {
    }

    /**
     * Basic play style that simply plays the first card in the hand against the
     * first monster in the enemies board if target-able
     *
     * @param allyBoard      current player's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played so far in the turn
     * @return The action the play style chooses to execute
     */
    public static Action basicAI(Board allyBoard, Board enemyBoard, int numCardsPlayed) {
        ActionBuilder actionBuilder = new BasicAIBuilder();
        return buildAction(actionBuilder, allyBoard, enemyBoard, numCardsPlayed);
    }

    /**
     * A Monster favouring play style that simply plays the first {@link Monster}
     * card in the hand against the
     * lowest life monster on the enemies board if target-able
     *
     * @param allyBoard      current player's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played so far in the turn
     * @return The action the play style chooses to execute
     */
    public static Action monsterFavouringAI(Board allyBoard, Board enemyBoard, int numCardsPlayed) {
        ActionBuilder actionBuilder = new MonsterFavouringAIBuilder();
        return buildAction(actionBuilder, allyBoard, enemyBoard, numCardsPlayed);
    }

    /**
     * A Set-up favouring play style that plays up to 3 cards in a turn.
     * If there are no {@link Spell}s or {@link Trap}s in the hand it will not play
     * a card unless the
     * <b>turnsToSetup</b> count has reached 0
     *
     * @param allyBoard      current player's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played so far in the turn
     * @return The action the play style chooses to execute
     */
    public static Action setupFavouringAI(Board allyBoard, Board enemyBoard, int numCardsPlayed) {
        ActionBuilder actionBuilder = new SetupFavouringAIBuilder();
        return buildAction(actionBuilder, allyBoard, enemyBoard, numCardsPlayed);
    }

    /**
     * A Reckless play style that has no limit on cards played each turn.
     * The play style will wait for a set number of turns and only play cards after
     * the <b>turnsToWait</b> count has
     * reached 0 in hopes of overwhelming the enemy
     *
     * @param allyBoard      current player's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played so far in the turn
     * @return The action the play style chooses to execute
     */
    public static Action recklessAI(Board allyBoard, Board enemyBoard, int numCardsPlayed) {
        ActionBuilder actionBuilder = new RecklessAIBuilder();
        return buildAction(actionBuilder, allyBoard, enemyBoard, numCardsPlayed);
    }
    
    /**
     * Sets up the action builder with the given parameters and builds the action
     * @param actionBuilder  action builder instance
     * @param allyBoard      ally's board
     * @param enemyBoard     enemy's board
     * @param numCardsPlayed number of cards played
     * @return The configured action
     */
    private static Action buildAction(
        ActionBuilder actionBuilder,
        Board allyBoard,
        Board enemyBoard,
        int numCardsPlayed
    ) {
        return actionBuilder
            .setAllyBoard(allyBoard)
            .setEnemyBoard(enemyBoard)
            .setNumCardsPlayed(numCardsPlayed)
            .build();
    }
}
