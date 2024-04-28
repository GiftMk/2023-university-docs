package uc.seng301.cardbattler.asg4.cucumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uc.seng301.cardbattler.asg4.cards.CardProxy;
import uc.seng301.cardbattler.asg4.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg4.game.GameInterface;
import uc.seng301.cardbattler.asg4.model.Player;

public class CreatePlayerFeature {
    private String playerName;
    private String playerPlayStyle;

    private GameInterface gameInterface;
    private List<String> output;
    Iterator<String> toMock;

    @Before
    public void setup() {
        CommandLineInterface cli = Mockito.mock(CommandLineInterface.class);
        gameInterface = new GameInterface(new CardProxy(), cli);
        toMock = Collections.emptyIterator();
        Mockito.when(cli.getNextLine()).thenAnswer(i -> toMock.next());
        output = new ArrayList<>();

        Mockito.doAnswer((i) -> {
            output.add(i.getArgument(0));
            // custom printer for debugging purposes
            System.out.println((String) i.getArgument(0));
            return null;
        }).when(cli).printLine(Mockito.anyString());
    }

    private void addInputMocking(String... mockedInputs) {
        toMock = Arrays.asList(mockedInputs).iterator();
    }

    @Given("I want to be named {string} with play style {string}")
    public void i_want_to_be_named_with_play_style(String name, String playStyle) {
        this.playerName = name;
        this.playerPlayStyle = playStyle;
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isEmpty());
        Assertions.assertNotNull(playStyle);
        Assertions.assertFalse(playStyle.isEmpty());
    }

    @When("I create a player")
    public void i_create_a_player() {
        addInputMocking(String.format("create_player %s %s", playerName, playerPlayStyle), "!q");
        gameInterface.play();
    }

    @Then("My player is registered correctly")
    public void my_player_is_registered_correctly() {
        Player foundPlayer = gameInterface.getPlayerAccessor().getPlayerByName(playerName);
        Assertions.assertNotNull(foundPlayer);
        Assertions.assertEquals(foundPlayer.getName(), playerName);
        Assertions.assertNotNull(foundPlayer.getPlayerAI());
        Assertions.assertTrue(output.get(output.size() - 2).contains(String.format("Created player %s", playerName)));
    }

    @Then("I can interact with other functionality")
    public void i_can_interact_with_other_functionality() {
        addInputMocking(String.format("print %s", playerName), "!q");
        gameInterface.play();
        Assertions.assertTrue(output.get(output.size() - 2).contains(String.format("Player (%s)", playerName)));

    }

    @Then("The player is not registered")
    public void the_player_is_not_registered() {
        Player foundPlayer = gameInterface.getPlayerAccessor().getPlayerByName(playerName);
        Assertions.assertNull(foundPlayer);
    }

    @Then("I am shown an error message telling me the name is invalid")
    public void i_am_shown_an_error_message_telling_me_the_name_is_invalid() {
        Assertions.assertTrue(output.get(output.size() - 2)
                .contains("Player name must be alphanumerical but cannot only be numeric"));
    }

    @Then("I am shown an error message telling me the play style is invalid")
    public void i_am_shown_an_error_message_telling_me_the_play_style_is_invalid() {
        Assertions.assertTrue(
                output.get(output.size() - 2).contains("Play style must match one of the expected options"));
    }

}
