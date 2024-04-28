package uc.seng301.cardbattler.lab5.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uc.seng301.cardbattler.lab5.accessor.CardAccessor;
import uc.seng301.cardbattler.lab5.accessor.DeckAccessor;
import uc.seng301.cardbattler.lab5.accessor.PlayerAccessor;
import uc.seng301.cardbattler.lab5.model.Card;
import uc.seng301.cardbattler.lab5.model.Deck;
import uc.seng301.cardbattler.lab5.model.Player;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateNewDeckFeature {
    
    private DeckAccessor deckAccessor;
    private PlayerAccessor playerAccessor;
    private CardAccessor cardAccessor;
    private Deck deck;
    private Player player;
    private Card card;
    
    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        deckAccessor = new DeckAccessor(sessionFactory);
        playerAccessor = new PlayerAccessor(sessionFactory);
        cardAccessor = new CardAccessor(sessionFactory);
    }
    
    @Given("I create a player named {string}")
    public void i_create_a_player_named(String name) {
        player = playerAccessor.createPlayer(name);
        assertNotNull(player);
    }
    
    @Given("I create a card named {string} with attack: {int}, defence: {int}, life: {int}")
    public void i_create_a_card_named_with_attack_with_defence_with_life(
        String name,
        int attack,
        int defence,
        int life
    ) {
        card = cardAccessor.createCard(name, attack, defence, life);
        assertNotNull(card);
    }
    
    @When("I create a deck named {string} with player: {"Gift Mkwara"}, cards: [card]")
}
