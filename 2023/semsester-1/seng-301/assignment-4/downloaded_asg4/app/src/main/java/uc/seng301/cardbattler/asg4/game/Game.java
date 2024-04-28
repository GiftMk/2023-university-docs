package uc.seng301.cardbattler.asg4.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uc.seng301.cardbattler.asg4.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg4.game.actions.Action;
import uc.seng301.cardbattler.asg4.model.Card;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Player;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

/**
 * Class handling battling or "playing a game of Yu-Gi-Oh!" between two players
 * Implementation of how cards interact is not exactly like Yu--Gi-Oh!, however
 * we have taken
 * some liberty in execution for simplicity
 */
public class Game {
    private static final Logger LOGGER = LogManager.getLogger(Game.class);

    private Board board1;
    private Board board2;
    private Player player1;
    private Player player2;

    private final List<Card> allCards;
    private final int turnLimit;
    private final CommandLineInterface cli;

    /**
     * Create a new game to be played
     * 
     * @param turnLimit number of turns to play
     * @param cli       command line interface to print to (no reading happens)
     */
    public Game(int turnLimit, CommandLineInterface cli) {
        allCards = new ArrayList<>();
        this.turnLimit = turnLimit;
        this.cli = cli;
    }

    /**
     * Public constructor with default turn count
     * 
     * @param cli command line interface to print to (no reading happens)
     */
    public Game(CommandLineInterface cli) {
        this(10, cli);
    }

    /**
     * Main game application loop
     */
    public void startGame() {
        if (player1 == null || player2 == null) {
            cli.printLine("Required player(s) are missing");
            LOGGER.error("Attempted to start a game when one or more players were null");
            return;
        }
        LOGGER.debug("Starting game between {} and {}. Will last for {} turns", player1.getName(), player2.getName(),
                turnLimit);
        cli.printLine("STARTING GAME...");
        board1.draw(5);
        board2.draw(5);
        boolean playing = true;
        int turn = 1;
        while (playing) {
            LOGGER.debug("Starting turn {}", turn);
            cli.printLine("-----STARTING TURN " + turn + "-----");
            board1.draw(1);
            int numCardsPlayed = 0;
            LOGGER.debug("Getting {}'s actions for turn {}", player1.getName(), turn);
            Action action = handlePlayerTurn(player1, numCardsPlayed);
            while (action != null) {
                cli.printLine("[P1 ACTION] " + action.toString());
                numCardsPlayed++;
                action = handlePlayerTurn(player1, numCardsPlayed);
            }

            board2.draw(1);
            numCardsPlayed = 0;
            LOGGER.debug("Getting {}'s actions for turn {}", player2.getName(), turn);
            action = handlePlayerTurn(player2, numCardsPlayed);
            while (action != null) {
                cli.printLine("[P2 ACTION] " + action.toString());
                numCardsPlayed++;
                action = handlePlayerTurn(player2, numCardsPlayed);
            }
            cli.printLine("-----BOARD STATES-----");
            cli.printLine("-----BOARD 1-----");
            cli.printLine(board1.getDisplayableSlots(0));

            cli.printLine("-----BOARD 2-----");
            cli.printLine(board2.getDisplayableSlots(0));
            turn++;
            if (turn > turnLimit) {
                playing = false;
            }
        }
        Player p = getWinner();
        if (p == null)
            cli.printLine("Game ended in a draw");
        else
            cli.printLine(String.format("WINNER IS %s, Congratulations!!!", p.getName()));

    }

    /**
     * Accepts input for a players turn and executes it
     * 
     * @param player         player whose turn it is
     * @param numCardsPlayed number of cards played so far this turn
     * @return the action the player completed (null if not action and turn should
     *         end)
     */
    private Action handlePlayerTurn(Player player, int numCardsPlayed) {
        Board allyBoard;
        Board enemyBoard;
        if (player.equals(player1)) {
            allyBoard = board1;
            enemyBoard = board2;
        } else {
            allyBoard = board2;
            enemyBoard = board1;
        }
        Action action = player.getPlayerAI().execute(allyBoard, enemyBoard, numCardsPlayed);
        if (action == null || action.getActor() == null)
            return null;
        this.listenForActions(action.getActor()); // only cards that are played to the board become subscribers
        allyBoard.playCard(action.getActor());
        actionTrigger(PlayState.BEFORE_PLAY, action.getActor(), action.getTarget());
        actionTrigger(PlayState.ON_PLAY, action.getActor(), action.getTarget());
        actionTrigger(PlayState.AFTER_PLAY, action.getActor(), action.getTarget());
        return action;
    }

    /**
     * Decides a winner based on the total amount of health of remaining monsters on
     * the board
     * 
     * @return the winning player
     */
    private Player getWinner() {
        int p1Life = board1.getMonsterSlots().stream().mapToInt(Monster::getLife).sum();
        int p2Life = board2.getMonsterSlots().stream().mapToInt(Monster::getLife).sum();
        if (p1Life == p2Life)
            return null;
        else if (p1Life > p2Life)
            return player1;
        else
            return player2;
    }

    /**
     * Add the first player
     * 
     * @param player1 Player to set as player 1
     * @param player2 Player to set as player 2
     */
    public void addPlayers(Player player1, Player player2) {
        this.player1 = player1;
        board1 = new Board(player1.getDeck());
        this.player2 = player2;
        board2 = new Board(player2.getDeck());
    }

    /**
     * Subscribe card to game for events
     * 
     * @param card Card to be subscribed
     */
    public void listenForActions(Card card) {
        LOGGER.debug("{} listening for actions", card);
        allCards.add(card);
    }

    /**
     * Stop card from listenting to actions
     * 
     * @param card Card to stop listening for actions
     */
    public void stopListeningForActions(Card card) {
        LOGGER.debug("{} no longer listening for actions", card);
        boolean removed = allCards.remove(card);
        if (!removed) {
            LOGGER.warn("{} was not removed from list of cards listening to actions", card);
        }
    }

    /**
     * Notify each subscribed card of q new playstate event
     * 
     * @param playState current playstate
     * @param actor     card making the action
     * @param target    card targeted by the action
     */
    public void actionTrigger(PlayState playState, Card actor, Card target) {
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card c : allCards) {
            boolean actorIsAlly = board1.cardsAreAllies(c, actor) || board2.cardsAreAllies(c, actor);
            boolean targetIsAlly = target != null
                    && (board1.cardsAreAllies(c, target) || board2.cardsAreAllies(c, target));
            if (!c.reactToAction(playState, actor, target, actorIsAlly, targetIsAlly))
                cardsToRemove.add(c);
        }
        cardsToRemove.forEach(this::stopListeningForActions);
        cardsToRemove.forEach(c -> removePlayedCard(c, board1, board2));
    }

    /**
     * Attempts to remove card from slots in given board
     * Could be extended to move these cards to a graveyard/discard pile
     * 
     * @param card   card to be removed
     * @param boards boards to check for removal
     */
    private void removePlayedCard(Card card, Board... boards) {
        boolean removed = false;
        for (Board board : boards) {
            if (card instanceof Monster monster && board.getMonsterSlots().remove(monster))
                removed = true;
            if (card instanceof Spell spell && board.getSpellSlots().remove(spell))
                removed = true;
            if (card instanceof Trap trap && board.getTrapSlots().remove(trap))
                removed = true;
        }
        if (removed)
            cli.printLine(String.format("[Card Removed] %s", card.getName()));
        else {
            LOGGER.fatal("Could not remove card {}", card.getName());
            throw new GameRuntimeException("Card could not be removed");
        }
    }
}
