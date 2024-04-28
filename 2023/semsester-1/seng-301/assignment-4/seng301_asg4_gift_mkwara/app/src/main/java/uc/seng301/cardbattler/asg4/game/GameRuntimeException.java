package uc.seng301.cardbattler.asg4.game;

/**
 * Simple runtime exception when an error occurs during the game
 */
public class GameRuntimeException extends RuntimeException {

    /**
     * Default constructor
     * 
     * @param message the reason for this runtime exception
     */
    public GameRuntimeException(String message) {
        super(message);
    }
}
