package uc.seng301.cardbattler.asg4.cucumber;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uc.seng301.cardbattler.asg4.cards.CardService;
import uc.seng301.cardbattler.asg4.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg4.game.GameInterface;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Monster;
import uc.seng301.cardbattler.asg4.model.Player;
import uc.seng301.cardbattler.asg4.model.Spell;
import uc.seng301.cardbattler.asg4.model.Trap;

public class BattleDeckFeature {
    private CardService offlineCardGenerator;

    private Player player;
    private Deck deck;
    private String playerName;

    private GameInterface gameInterface;
    private CommandLineInterface cli;

    @Before
    public void setup() {
        offlineCardGenerator = Mockito.spy(new CardService());
        cli = Mockito.mock(CommandLineInterface.class);
        gameInterface = new GameInterface(offlineCardGenerator, cli);
        Mockito.doAnswer((i) -> {
            // custom printer for debugging purposes
            System.out.println((String) i.getArgument(0));
            return null;
        }).when(cli).printLine(Mockito.anyString());
    }

    private void addInputMocking(String... mockedInputs) {
        Iterator<String> toMock = Arrays.asList(mockedInputs).iterator();
        Mockito.when(cli.getNextLine()).thenAnswer(i -> toMock.next());
    }

    @Given("The player {string} exists")
    public void the_player_exists(String playerName) {
        player = gameInterface.getPlayerAccessor().createPlayer(playerName, "basic");
        this.playerName = playerName;
        boolean wasSaved = gameInterface.getPlayerAccessor().persistPlayer(player);
        Assertions.assertNotNull(player);
        Assertions.assertTrue(wasSaved);
        Assertions.assertSame(player.getName(), playerName);
    }

    @Given("The player has no deck")
    public void there_is_no_deck_named() {
        Assertions.assertNull(gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck());
    }

    @When("I create a battle deck named {string}")
    public void i_create_a_battle_deck(String deckName) {
        addInputMocking(String.format("battle_deck %s %s", player.getName(), deckName), "r", "!q");
        Mockito.doReturn(null).when(offlineCardGenerator).getResponseFromAPI(Mockito.any());
        gameInterface.play();
    }

    @When("I create a battle deck named {string} with {int} monsters, {int} spells and {int} traps")
    public void i_create_a_battle_deck_named_with_monsters_spells_and_traps(String deckName, Integer numMonsters,
            Integer numSpells, Integer numTraps) {
        addInputMocking(String.format("battle_deck %s %s", player.getName(), deckName), "c", numMonsters.toString(),
                numSpells.toString(), numTraps.toString(), "!q");
        Mockito.doReturn(null).when(offlineCardGenerator).getResponseFromAPI(Mockito.any());
        gameInterface.play();
    }

    @Then("The battle deck must contain {int} cards exactly")
    public void the_battle_deck_must_contain_twenty_cards_exactly(Integer numCards) {
        deck = gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck();
        Assertions.assertNotNull(deck);
        Assertions.assertEquals(numCards, deck.getCards().size());
    }

    @Then("The battle deck contains at least {int} monsters")
    public void the_battle_deck_contains_at_least_monsters(Integer numMonsters) {
        deck = gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck();
        Assertions
                .assertTrue(numMonsters <= deck.getCards().stream().filter(c -> c instanceof Monster).toList().size());
    }

    @Then("The battle deck contains at least {int} spells")
    public void the_battle_deck_contains_at_least_spells(Integer numSpells) {
        deck = gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck();
        Assertions.assertTrue(numSpells <= deck.getCards().stream().filter(c -> c instanceof Spell).toList().size());
    }

    @Then("The battle deck contains at least {int} traps")
    public void the_battle_deck_contains_at_least_traps(Integer numTraps) {
        deck = gameInterface.getPlayerAccessor().getPlayerByName(playerName).getDeck();
        Assertions.assertTrue(numTraps <= deck.getCards().stream().filter(c -> c instanceof Trap).toList().size());
    }

}
