/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package uc.seng301.cardbattler.lab4;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import uc.seng301.cardbattler.lab4.model.Card;
import uc.seng301.cardbattler.lab4.model.Deck;
import uc.seng301.cardbattler.lab4.model.Player;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final SessionFactory sessionFactory;
    static {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        System.out.println("Persisting a player with a deck of cards");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // creating entities
            Card cardA = new Card("My Card A", 100, 100, 100);
            Card cardB = new Card("My Card A", 100, 100, 100);
            Deck deck = new Deck("My Deck", List.of(cardA, cardB));
            Player player = new Player("Gift", List.of(deck));
            // persisting entities
            transaction = session.beginTransaction();
            session.persist(player);
            session.persist(cardA);
            session.persist(cardB);
            session.persist(deck);
            transaction.commit();
            // retrieving entities
            Card savedCard = session
                    .createQuery(
                            "FROM Card where cardId = " + cardA.getCardId(),
                            Card.class
                    ).uniqueResult();
            Deck savedDeck = session.createQuery(
                    "FROM Deck where deckId = " + deck.getDeckId(),
                    Deck.class
            ).uniqueResult();
            Player savedPlayer = session
                    .createQuery(
                            "FROM Player where playerId = " + player.getPlayerId(),
                            Player.class
                    ).uniqueResult();
            // printing saved entities
            System.out.println(savedCard);
            System.out.println(savedDeck);
            System.out.println(savedPlayer);
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        }
    }
}