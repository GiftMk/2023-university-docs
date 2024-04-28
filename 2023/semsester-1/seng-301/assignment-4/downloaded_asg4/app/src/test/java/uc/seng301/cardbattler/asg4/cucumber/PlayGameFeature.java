package uc.seng301.cardbattler.asg4.cucumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uc.seng301.cardbattler.asg4.cards.CardService;
import uc.seng301.cardbattler.asg4.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg4.game.BattleDeckCreator;
import uc.seng301.cardbattler.asg4.game.GameInterface;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Player;

public class PlayGameFeature {
    private GameInterface gameInterface;
    private CommandLineInterface cli;
    private BattleDeckCreator battleDeckCreator;
    private List<String> output;

    @Before
    public void setup() {
        CardService offlineCardGenerator = Mockito.spy(new CardService());
        cli = Mockito.mock(CommandLineInterface.class);
        battleDeckCreator = new BattleDeckCreator(offlineCardGenerator);
        gameInterface = new GameInterface(offlineCardGenerator, cli);
        output = new ArrayList<>();

        Mockito.doAnswer((i) -> {
            output.add(i.getArgument(0));
            // custom printer for debugging purposes
            System.out.println((String) i.getArgument(0));
            return null;
        }).when(cli).printLine(Mockito.anyString());
    }

    private void addInputMocking(String... mockedInputs) {
        Iterator<String> toMock = Arrays.asList(mockedInputs).iterator();
        Mockito.when(cli.getNextLine()).thenAnswer(i -> toMock.next());
    }

    @Given("A player {string} exists with a valid battle deck")
    public void a_player_exists_with_a_valid_battle_deck(String playerName) {
        Player player = gameInterface.getPlayerAccessor().createPlayer(playerName, "basic");
        Assertions.assertNotNull(player);
        Deck deck = gameInterface.getDeckAccessor().createDeck(String.format("%ss_deck", playerName), player,
                new ArrayList<>());
        battleDeckCreator.populateRandomBattleDeck(deck);
        gameInterface.getPlayerAccessor().persistPlayer(player);
        Assertions.assertEquals(player, gameInterface.getPlayerAccessor().getPlayerByName(playerName));
        Assertions.assertNotNull(gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck());
        Assertions.assertFalse(
                gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck().getCards().isEmpty());
    }

    @When("I, {string}, battle {string}")
    public void i_battle(String playerName, String otherPlayerName) {
        addInputMocking(String.format("play_game %s %s", playerName, otherPlayerName), "!q");
        gameInterface.play();
    }

    @Then("We battle?")
    public void we_battle() {
        Assertions.assertEquals(1, output.stream().filter(s -> s.equals("STARTING GAME...")).toList().size());
    }

    @Given("A player {string} exists without a valid battle deck")
    public void a_player_exists_without_a_valid_battle_deck(String playerName) {
        Player player = gameInterface.getPlayerAccessor().createPlayer(playerName, "basic");
        Assertions.assertNotNull(player);
        gameInterface.getDeckAccessor().createDeck(String.format("%ss_deck", playerName), player, new ArrayList<>());
        gameInterface.getPlayerAccessor().persistPlayer(player);
        Assertions.assertEquals(player, gameInterface.getPlayerAccessor().getPlayerByName(playerName));
        Assertions.assertTrue(
                gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck().getCards().isEmpty());
    }

    @Then("I am informed that the battle could not start for reason {string}")
    public void i_am_informed_that_the_battle_could_not_start(String reason) {
        Assertions.assertTrue(output.get(output.size() - 2).contains(reason));
    }

    @Then("The battle goes on for {int} turns")
    public void the_battle_goes_on_for_turns(Integer numberOfTurns) {
        Assertions.assertEquals(1,
                output.stream().filter(s -> s.contains("STARTING TURN " + numberOfTurns)).toList().size());
        Assertions.assertEquals(0,
                output.stream().filter(s -> s.contains("STARTING TURN " + (numberOfTurns + 1))).toList().size());
    }

    @Then("A winner is decided or the game ends in a draw")
    public void a_winner_is_decided_or_the_game_ends_in_a_draw() {
        Assertions.assertTrue(output.get(output.size() - 2).contains("WINNER IS")
                || output.get(output.size() - 2).equals("Game ended in a draw"));
    }

}
