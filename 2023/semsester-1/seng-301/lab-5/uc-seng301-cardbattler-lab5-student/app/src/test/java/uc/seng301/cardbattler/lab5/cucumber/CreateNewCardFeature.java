package uc.seng301.cardbattler.lab5.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uc.seng301.cardbattler.lab5.accessor.CardAccessor;
import uc.seng301.cardbattler.lab5.model.Card;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateNewCardFeature {

    private CardAccessor cardAccessor;
    private Card card;
    private Exception expectedException;
    private String cardName;
    private int cardAttack;
    private int cardDefence;
    private int cardLife;

    @Before
    public void setUp() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        cardAccessor = new CardAccessor(sessionFactory);
    }

    @Given("There is no card with name {string}")
    public void there_is_no_card_with_name(String string) {
        assertNull(cardAccessor.getCardByName(string));
    }

    @When("I create a card named {string} with attack: {int}, defence: {int}, life: {int}")
    public void i_create_a_card_named_with_attack_defence_life(
            String cardName,
            Integer cardAttack,
            Integer cardDefence,
            Integer cardLife
    ) {
        this.cardName = cardName;
        this.cardAttack = cardAttack;
        this.cardDefence = cardDefence;
        this.cardLife = cardLife;
        assertNotNull(cardName);
        assertTrue(cardAttack > 0);
        assertTrue(cardDefence > 0);
        assertTrue(cardLife >= 0);
    }

    @Then("The card is created with the correct name, attack, defence, and life")
    public void the_card_is_created_with_the_correct_name_attack_defence_and_life() {
        card = cardAccessor.createCard(cardName, cardAttack, cardDefence, cardLife);
        assertNotNull(card);
        assertEquals(cardName, card.getName());
        assertEquals(cardAttack, card.getAttack());
        assertEquals(cardDefence, card.getDefence());
        assertEquals(cardLife, card.getLife());
    }

    @When("I create an invalid card named {string} with attack: {int}, defence: {int}, life: {int}")
    public void i_create_an_invalid_card_named_with_attack_defence_life(
        String cardName,
        int cardAttack,
        int cardDefence,
        int cardLife
    ) {
        expectedException = assertThrows(
            IllegalArgumentException.class,
            () -> cardAccessor.createCard(cardName, cardAttack, cardDefence, cardLife)
        );
    }
    
    @Then("An exception is thrown")
    public void an_exception_is_thrown() {
        assertNotNull(expectedException);
    }
}
