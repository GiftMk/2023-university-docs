package uc.seng301.cardbattler.lab5.accessor;

import org.hibernate.*;
import uc.seng301.cardbattler.lab5.model.Card;
import uc.seng301.cardbattler.lab5.model.Deck;
import uc.seng301.cardbattler.lab5.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class offers helper methods for saving, removing, and fetching deck records from persistence
 * {@link Deck} entities
 */
public class DeckAccessor {
    private final SessionFactory sessionFactory;

    /**
     * default constructor
     * @param sessionFactory the JPA session factory to talk to the persistence implementation.
     */
    public DeckAccessor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Create a {@link Deck} object with the given parameters
     * @param name The Deck name must be [a-zA-Z0-9 ] (not null, empty, or only numerics)
     * @param player The Player whose deck it is (cannot be null)
     * @param cards The cards to be in the deck, must not be null and must have at least 1 card
     * @return The Deck object with given parameters
     * @throws IllegalArgumentException If any of the above preconditions for input arguments are violated
     */
    public Deck createDeck(String name, Player player, List<Card> cards) {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (name.matches("[0-9]+") || !name.matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("Name must be alphanumerical but cannot only be numeric");
        }
        if (cards.isEmpty()) {
            throw new IllegalArgumentException("Deck must contain at least one card");
        }
        Deck deck = new Deck();
        deck.setPlayer(player);
        deck.setName(name);
        deck.setCards(cards);
        return deck;
    }

    /**
     * Get game object from persistence layer
     * @param deckId id of deck to fetch
     * @return Deck with id given if it exists in database, no user object
     */
    public Deck getDeckById(Long deckId) {
        if (null == deckId) {
            throw new IllegalArgumentException("cannot retrieve deck with null id");
        }
        Deck deck = null;
        try (Session session = sessionFactory.openSession()) {
            deck = session.createQuery("FROM Deck WHERE deckId =" + deckId, Deck.class).uniqueResult();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return deck;
    }

    /**
     * Gets all decks belonging to player by id
     * @param playerId id of player to fetch decks
     * @return Decks belonging to player with id provided
     */
    public List<Deck> getPlayerDecksById(Long playerId) {
        List<Deck> decks = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            decks = session.createQuery("FROM Deck WHERE playerId="+playerId, Deck.class).list();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return decks;
    }

    /**
     * Save given deck to persistence
     * @param deck deck to be saved
     * @return The id of the persisted deck
     */
    public Long persistDeck(Deck deck) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(deck);
            transaction.commit();
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return deck.getDeckId();
    }

    /**
     * remove given deck from persistence by id
     * @param deckId id of deck to be deleted
     * @return true if the record is deleted
     */
    public boolean deleteDeckById(Long deckId) throws IllegalArgumentException {
        Deck deck = getDeckById(deckId);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(deck);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.err.println("Unable to open session or transaction");
            e.printStackTrace();
        }
        return false;
    }
}
