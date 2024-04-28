package uc.seng301.cardbattler.asg4.accessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uc.seng301.cardbattler.asg4.game.PlayStyles;
import uc.seng301.cardbattler.asg4.game.PlayerAIOperation;
import uc.seng301.cardbattler.asg4.model.Player;

/**
 * This class offers helper methods for creating {@link Player}s
 * Keeps a list of players in memory
 */
public class PlayerAccessor {
    private static final Logger LOGGER = LogManager.getLogger(PlayerAccessor.class);
    /**
     * In-memory persistence of {@link Player}s
     */
    private final List<Player> players;

    /**
     * default constructor
     */
    public PlayerAccessor() {
        players = new ArrayList<>();
    }

    /**
     * Create a {@link Player} object with the given parameters
     * 
     * @param name      The Player name must be [a-zA-Z0-9] (not null, empty, or
     *                  only
     *                  numerics)
     * @param playStyle The play style (or AI) the player should use when playing a
     *                  game {@link PlayStyles}
     * @return The Player object with given parameters
     * @throws IllegalArgumentException If any of the above preconditions for input
     *                                  arguments are violated
     */
    public Player createPlayer(String name, String playStyle) throws IllegalArgumentException {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }
        if (name.matches("\\d+") || !name.matches("\\w+")) {
            throw new IllegalArgumentException("Player name must be alphanumerical but cannot only be numeric");
        }
        PlayerAIOperation playerAIOperation;
        switch (playStyle) {
            case "basic" -> playerAIOperation = PlayStyles::basicAI;
            case "monster" -> playerAIOperation = PlayStyles::monsterFavouringAI;
            case "setup" -> playerAIOperation = PlayStyles::setupFavouringAI;
            case "reckless" -> playerAIOperation = PlayStyles::recklessAI;
            default -> throw new IllegalArgumentException("Play style must match one of the expected options");
        }
        Player player = new Player();
        player.setName(name);
        player.setPlayerAI(playerAIOperation);
        player.setDeck(null);
        return player;
    }

    /**
     * Gets player from memory by name
     * 
     * @param name name of player to fetch
     * @return Player with given name
     */
    public Player getPlayerByName(String name) {
        if (null == name || name.isBlank()) {
            throw new IllegalArgumentException("name '" + name + "' cannot be null or blank");
        }
        List<Player> matchingPlayers = players.stream().filter(p -> p.getName().equals(name)).toList();
        if (matchingPlayers.size() == 1)
            return matchingPlayers.get(0);
        else if (matchingPlayers.size() > 1)
            LOGGER.error("More than one matching player found");
        return null;
    }

    /**
     * Saves player to in-memory persistence
     * 
     * @param player player to save
     * @return id of saved player, or -1 if an error occurred
     * @throws IllegalArgumentException if player object is invalid (e.g. missing
     *                                  properties)
     */
    public boolean persistPlayer(Player player) throws IllegalArgumentException {
        if (null == player || null == player.getName() || player.getName().isBlank() || player.getPlayerAI() == null) {
            throw new IllegalArgumentException("cannot save null or blank player");
        }

        Player existingPlayer = getPlayerByName(player.getName());
        if (existingPlayer != null) {
            LOGGER.error("Player already saved in memory");
            return false;
        }
        players.add(player);
        return true;
    }

    /**
     * remove given player from persistence by id
     * 
     * @param name name of player to be deleted
     * @return true if the record is deleted
     */
    public boolean removePlayerByName(String name) {
        Player player = getPlayerByName(name);
        if (player == null) {
            LOGGER.warn("No player with name {} found to remove", name);
            return false;
        }
        players.remove(player);
        return true;
    }
}
