package uc.seng301.cardbattler.asg3.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import uc.seng301.cardbattler.asg3.accessor.DeckAccessor;
import uc.seng301.cardbattler.asg3.accessor.PlayerAccessor;
import uc.seng301.cardbattler.asg3.cards.CardService;
import uc.seng301.cardbattler.asg3.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg3.game.Game;
import uc.seng301.cardbattler.asg3.model.Deck;
import uc.seng301.cardbattler.asg3.model.Monster;
import uc.seng301.cardbattler.asg3.model.Player;
import uc.seng301.cardbattler.asg3.model.Spell;
import uc.seng301.cardbattler.asg3.model.Trap;

import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.CHOICE_INPUT;
import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.RANDOM_INPUT;
import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.addInputMocking;
import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.getBattleDeckCommand;

public class CreateNewBattleDeckFeature {
    
    private SessionFactory sessionFactory;
    private PlayerAccessor playerAccessor;
    private DeckAccessor deckAccessor;
    private CardService cardGeneratorSpy;
    private CommandLineInterface cli;
    private Game game;
    private Player player;
    private Deck deck;
    
    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        playerAccessor = new PlayerAccessor(sessionFactory);
        deckAccessor = new DeckAccessor(sessionFactory);
        cardGeneratorSpy = Mockito.spy(new CardService());
        Mockito
            .doReturn("")
            .when(cardGeneratorSpy)
            .getResponseFromAPI(Mockito.any());
        cli = Mockito.mock(CommandLineInterface.class);
    }
    
    @Given("The player {string} exists")
    public void the_player_exists(String playerName) {
        player = playerAccessor.createPlayer(playerName);
        Long playerId = playerAccessor.persistPlayer(player);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(playerId);
        Assertions.assertSame(player.getName(), playerName);
    }
    
    @Given("There is no deck named {string}")
    public void there_is_no_deck_named(String deckName) {
        Assertions.assertNull(deckAccessor.getDeckByName(deckName));
    }
    
    /**
     * Asserts that a deck is not null,
     * that the deck's id, name and cards are not null
     * and that the deck's player is not null and has the correct id
     * @param deck the deck to make assertions on
     */
    private void assertDeckWasProperlyPopulated(Deck deck) {
        Assertions.assertNotNull(deck);
        Assertions.assertNotNull(deck.getDeckId());
        Assertions.assertNotNull(deck.getPlayer());
        Assertions.assertSame(player.getPlayerId(), deck.getPlayer().getPlayerId());
        Assertions.assertNotNull(deck.getName());
        Assertions.assertNotNull(deck.getCards());
    }
    
    @When("I create a battle deck named {string}")
    public void i_create_a_battle_deck_named(String deckName) {
        game = new Game(cardGeneratorSpy, cli, sessionFactory);
        Assertions.assertNotNull(game);
        addInputMocking(cli, RANDOM_INPUT);
        game.battleDeck(getBattleDeckCommand(player.getName(), deckName));
        deck = deckAccessor.getDeckByPlayerNameAndDeckName(player.getName(), deckName);
        assertDeckWasProperlyPopulated(deck);
    }
    
    @When("I create a battle deck named {string} with {int} monsters, {int} spells and {int} traps")
    public void i_create_a_battle_deck_named_with_monsters_spells_and_traps(
        String deckName,
        Integer numMonsters,
        Integer numSpells,
        Integer numTraps
    ) {
        game = new Game(cardGeneratorSpy, cli, sessionFactory);
        Assertions.assertNotNull(game);
        addInputMocking(
            cli,
            CHOICE_INPUT,
            String.valueOf(numMonsters),
            String.valueOf(numSpells),
            String.valueOf(numTraps)
        );
        game.battleDeck(getBattleDeckCommand(player.getName(), deckName));
        deck = deckAccessor.getDeckByPlayerNameAndDeckName(player.getName(), deckName);
        assertDeckWasProperlyPopulated(deck);
    }
    
    @Then("The battle deck must contain {int} cards exactly")
    public void the_battle_deck_must_contain_cards_exactly(Integer expectedNumberOfCards) {
        Assertions.assertEquals(expectedNumberOfCards, deck.getCards().size());
    }
    
    @Then("The battle deck contains at least {int} monsters")
    public void the_battle_deck_contains_at_least_monsters(Integer minNumberOfMonsters) {
        int numMonsters = (int) deck.getCards()
            .stream()
            .filter(c -> c instanceof Monster)
            .count();
        Assertions.assertTrue(numMonsters >= minNumberOfMonsters);
    }
    
    @Then("The battle deck contains at least {int} spells")
    public void the_battle_deck_contains_at_least_spells(Integer minNumberOfSpells) {
        int numSpells = (int) deck.getCards()
            .stream()
            .filter(c -> c instanceof Spell)
            .count();
        Assertions.assertTrue(numSpells >= minNumberOfSpells);
    }
    
    @Then("The battle deck contains at least {int} traps")
    public void the_battle_deck_contains_at_least_traps(Integer minNumOfTraps) {
        int numTraps = (int) deck.getCards()
            .stream()
            .filter(c -> c instanceof Trap)
            .count();
        Assertions.assertTrue(numTraps >= minNumOfTraps);
    }
}