package uc.seng301.cardbattler.lab6.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mockito.Mockito;
import uc.seng301.cardbattler.lab6.accessor.DeckAccessor;
import uc.seng301.cardbattler.lab6.accessor.PlayerAccessor;
import uc.seng301.cardbattler.lab6.cards.CardService;
import uc.seng301.cardbattler.lab6.cli.CommandLineInterface;
import uc.seng301.cardbattler.lab6.model.Card;
import uc.seng301.cardbattler.lab6.model.Deck;
import uc.seng301.cardbattler.lab6.model.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DrawRandomCardFeature {
    
    private PlayerAccessor playerAccessor;
    private DeckAccessor deckAccessor;
    private Player player;
    private Deck deck;
    private Card card;
    private CardService cardGeneratorSpy;
    private CommandLineInterface cli;
    
    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        playerAccessor = new PlayerAccessor(sessionFactory);
        deckAccessor = new DeckAccessor(sessionFactory);
        cardGeneratorSpy = Mockito.spy(new CardService());
        cli = Mockito.mock(CommandLineInterface.class);
        Mockito.when(cli.getNextLine()).thenReturn("Y");
    }
    
    @Given("I have a player {string}")
    public void i_have_a_player(String playerName) {
        player = playerAccessor.createPlayer(playerName);
        assertNotNull(player);
        assertEquals(playerName, player.getName());
    }
    
    @Given("I have an empty deck {string}")
    public void i_have_an_empty_deck(String deckName) {
        deck = deckAccessor.createDeck(deckName, player, List.of());
    }
    
    @When("I draw a randomly selected card")
    public void i_draw_a_randomly_selected_card() {
        String apiResponse = "{\"data\":[{\"id\":44073668,\"name\":\"Takriminos\",\"type\":\"Normal Monster\",\"frameType\":\" normal\",\"desc\":\"A member of a race of sea serpents that freely travels through the sea.\",\"atk\":1500,\"def \":1200,\"level\":4,\"race\":\"Sea Serpent\",\"attribute\":\"WATER\"}]}\n";
        Mockito.doReturn(apiResponse).when(cardGeneratorSpy).getResponseFromAPI();
        card = cardGeneratorSpy.getRandomCard();
        assertTrue(card.getLife() > 0);
    }
    
    @Then("I recieve a random monster card with valid stats")
    public void i_recieve_a_random_monster_card_with_valid_stats() {
        assertNotNull(card);
        assertTrue(card.getAttack() > 0);
        assertTrue(card.getDefence() > 0);
        assertEquals(0, card.getLife());
        assertFalse(card.getName() == null || card.getName().isEmpty());
    }
    
    @Given("While playing I draw a randomly selected card")
    public void while_playing_i_draw_a_randomly_selected_card() {
        cli.
    }
    
    @When("I decide I want to add the card to my deck")
    public void i_decide_i_want_to_add_the_card_to_my_deck() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    
    @Then("The card is added to my deck")
    public void the_card_is_added_to_my_deck() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    
    @When("I decide I want to ignore the card")
    public void i_decide_i_want_to_ignore_the_card() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    
    @Then("The card is not added to my deck")
    public void the_card_is_not_added_to_my_deck() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    
}
