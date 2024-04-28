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
import uc.seng301.cardbattler.asg3.game.GameBoard;
import uc.seng301.cardbattler.asg3.model.Card;
import uc.seng301.cardbattler.asg3.model.CardPosition;
import uc.seng301.cardbattler.asg3.model.Deck;
import uc.seng301.cardbattler.asg3.model.Monster;
import uc.seng301.cardbattler.asg3.model.Player;
import uc.seng301.cardbattler.asg3.model.Spell;
import uc.seng301.cardbattler.asg3.model.Trap;
import java.util.ArrayList;
import java.util.List;

import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.RANDOM_INPUT;
import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.addInputMocking;
import static uc.seng301.cardbattler.asg3.cucumber.TestUtils.getBattleDeckCommand;

public class PlaceCardsFeature {
    
    private SessionFactory sessionFactory;
    private PlayerAccessor playerAccessor;
    private DeckAccessor deckAccessor;
    private CardService cardGeneratorSpy;
    private CommandLineInterface cli;
    private GameBoard gameBoard;
    private List<Card> hand;
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
    
    @Given("A player exists named {string}")
    public void a_player_exists_named(String playerName) {
        player = playerAccessor.createPlayer(playerName);
        Long playerId = playerAccessor.persistPlayer(player);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(playerId);
        Assertions.assertSame(player.getName(), playerName);
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
    
    @Given("I have a battle deck named {string}")
    public void i_have_a_battle_deck_named(String deckName) {
        Game game = new Game(cardGeneratorSpy, cli, sessionFactory);
        Assertions.assertNotNull(game);
        addInputMocking(cli, RANDOM_INPUT);
        game.battleDeck(getBattleDeckCommand(player.getName(), deckName));
        deck = deckAccessor.getDeckByPlayerNameAndDeckName(player.getName(), deckName);
        assertDeckWasProperlyPopulated(deck);
    }
    
    @When("The game has started")
    public void the_game_has_started() {
        gameBoard = new GameBoard(deck);
        Assertions.assertNotNull(gameBoard);
        gameBoard.startGame();
        hand = new ArrayList<>(gameBoard.getHand());
    }
    
    @When("I play each card in my deck")
    public void i_play_each_card_in_my_deck() {
        for (Card card : hand) {
            gameBoard.playCard(card);
        }
        Assertions.assertTrue(gameBoard.getHand().isEmpty());
    }
    
    @Then("Each card is placed at its dedicated place on the board")
    public void each_card_is_placed_at_its_dedicated_place_on_the_board() {
        for (Card card : hand) {
            if (card instanceof Monster monster) {
                Assertions.assertNotNull(gameBoard.getMonsterSlots());
                Assertions.assertTrue(
                    gameBoard.getMonsterSlots().contains(monster)
                );
            }
            if (card instanceof Spell spell) {
                Assertions.assertNotNull(gameBoard.getSpellSlots());
                Assertions.assertTrue(
                    gameBoard.getSpellSlots().contains(spell)
                );
            }
            if (card instanceof Trap trap) {
                Assertions.assertNotNull(gameBoard.getTrapSlots());
                Assertions.assertTrue(
                    gameBoard.getTrapSlots().contains(trap)
                );
            }
        }
    }
    
    @Then("I draw {int} cards from my battle deck")
    public void i_draw_cards_from_my_battle_deck(Integer expectedNumCards) {
        Assertions.assertNotNull(gameBoard.getHand());
        Assertions.assertEquals(expectedNumCards, gameBoard.getHand().size());
    }
    
    @Then("Each monster's life is equal to its attack when in attack mode or its defense when in defense mode")
    public void each_monster_s_life_is_equal_to_its_attack_when_in_attack_mode_or_its_defense_when_in_defense_mode() {
        for (Card card : hand) {
            if (card instanceof Monster monster) {
                if (monster.getCardPosition().equals(CardPosition.ATTACK)) {
                    Assertions.assertEquals(monster.getAttack(), monster.getLife());
                } else if (monster.getCardPosition().equals(CardPosition.DEFEND)) {
                    Assertions.assertEquals(monster.getDefence(), monster.getLife());
                } else {
                    Assertions.fail();
                }
            }
        }
    }
}
