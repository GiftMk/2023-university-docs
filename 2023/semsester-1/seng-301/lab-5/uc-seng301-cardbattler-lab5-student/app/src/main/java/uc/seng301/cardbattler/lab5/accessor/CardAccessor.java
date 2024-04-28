package uc.seng301.cardbattler.lab5.accessor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import uc.seng301.cardbattler.lab5.model.Card;

/**
 * This class offers helper methods for saving, removing, and fetching card
 * records from persistence
 * {@link Card} entities
 */
public class CardAccessor {
    private final SessionFactory sessionFactory;

    /**
     * default constructor
     * 
     * @param sessionFactory the JPA session factory to talk to the persistence
     *                       implementation.
     */
    public CardAccessor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Create a {@link Card} object with the given parameters
     * 
     * @param name    the card name (not null, empty, or only numerics, can be
     *                [a-zA-Z0-9 ])
     * @param attack  attack value of card (must be > 0)
     * @param defence defence value of card (must be > 0)
     * @param life    life value of card (must be >= 0)
     * @return The Card object with given parameters
     * @throws IllegalArgumentException if any of the above preconditions for input
     *                                  arguments are violated
     */
    public Card createCard(String name, int attack, int defence, int life) {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (name.matches("[0-9]+") || !name.matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("Name must be alphanumerical but cannot only be numeric");
        }
        if (attack <= 0) {
            throw new IllegalArgumentException("Attack must be strictly positive");
        }
        if (defence <= 0) {
            throw new IllegalArgumentException("Defence must be strictly positive");
        }
        if (life < 0) {
            throw new IllegalArgumentException("Life cannot be negative");
        }
        Card card = new Card();
        card.setName(name);
        card.setAttack(attack);
        card.setDefence(defence);
        card.setLife(life);
        return card;
    }

    /**
     * Get game object from persistence layer
     * 
     * @param cardId id of card to fetch
     * @return Card with id given if it exists in database, no user object
     */
    public Card getCardById(Long cardId) {
        if (null == cardId) {
            throw new IllegalArgumentException("cannot retrieve card with null id");
        }
        Card card = null;
        try (Session session = sessionFactory.openSession()) {
            card = session.createQuery("FROM Card WHERE cardId =" + cardId, Card.class).uniqueResult();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return card;
    }

    /**
     * Get a card by its name
     * 
     * @param cardName a name to look for
     * @return the Card with given name if such card exists, null otherwise
     */
    public Card getCardByName(String cardName) {
        Card card = null;
        try (Session session = sessionFactory.openSession()) {
            card = session.createQuery("FROM Card WHERE name ='" + cardName + "'", Card.class).uniqueResult();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return card;
    }

    /**
     * Gets all cards belonging to player by id
     * 
     * @param DeckId id of player to fetch cards
     * @return Cards belonging to player with id provided
     */
    public List<Card> getDeckCardsById(Long DeckId) {
        List<Card> cards = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            cards = session.createQuery("FROM Card WHERE deckId=" + DeckId, Card.class).list();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Save given card to persistence
     * 
     * @param card card to be saved
     * @return The id of the persisted card
     */
    public Long persistCard(Card card) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(card);
            transaction.commit();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return card.getCardId();
    }

    /**
     * remove given card from persistence by id
     * 
     * @param cardId id of card to be deleted
     * @return true if the record is deleted
     */
    public boolean deleteCardById(Long cardId) throws IllegalArgumentException {
        Card card = getCardById(cardId);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(card);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return false;
    }

}
