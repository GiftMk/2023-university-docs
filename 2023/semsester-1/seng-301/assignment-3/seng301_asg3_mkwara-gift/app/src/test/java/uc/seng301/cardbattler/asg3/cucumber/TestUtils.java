package uc.seng301.cardbattler.asg3.cucumber;

import org.mockito.Mockito;
import uc.seng301.cardbattler.asg3.cli.CommandLineInterface;

import java.util.Arrays;
import java.util.Iterator;

public class TestUtils {
    
    public static final String RANDOM_INPUT = "random";
    public static final String CHOICE_INPUT = "choice";
    public static final String BATTLE_DECK_COMMAND = "battle_deck %s %s";
    
    /**
     * A helper method for getting a formatted battle deck command
     *
     * @param playerName the player name
     * @param deckName the deck name
     * @return a formatted battle deck command including the player name
     * and deck name
     */
    public static String getBattleDeckCommand(String playerName, String deckName) {
        return BATTLE_DECK_COMMAND.formatted(playerName, deckName);
    }
    
    /**
     * Adds any number of strings to input mocking FIFO
     * You may find this helpful for U4
     *
     * @param mockedInputs strings to add
     */
    public static void addInputMocking(CommandLineInterface cli, String... mockedInputs) {
        Iterator<String> toMock = Arrays.asList(mockedInputs).iterator();
        Mockito.when(cli.getNextLine()).thenAnswer(i -> toMock.next());
    }
}
