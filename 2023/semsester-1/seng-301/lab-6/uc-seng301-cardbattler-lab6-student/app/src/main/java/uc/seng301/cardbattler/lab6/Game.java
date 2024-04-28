package uc.seng301.cardbattler.lab6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uc.seng301.cardbattler.lab6.accessor.CardAccessor;
import uc.seng301.cardbattler.lab6.accessor.DeckAccessor;
import uc.seng301.cardbattler.lab6.accessor.PlayerAccessor;
import uc.seng301.cardbattler.lab6.cards.CardGenerator;
import uc.seng301.cardbattler.lab6.cards.CardProxy;
import uc.seng301.cardbattler.lab6.cli.CommandLineInterface;
import uc.seng301.cardbattler.lab6.model.Card;
import uc.seng301.cardbattler.lab6.model.Deck;
import uc.seng301.cardbattler.lab6.model.Player;

import java.util.ArrayList;

/**
 * Main game loop functionality for application
 */
public class Game {
    private static final Logger LOGGER = LogManager.getLogger(Game.class);
    private final CommandLineInterface cli;
    private final PlayerAccessor playerAccessor;
    private final DeckAccessor deckAccessor;
    private final CardAccessor cardAccessor;
    private final CardGenerator cardGenerator;

    /**
     * Create a new game with default settings
     */
    public Game() {
        // this will load the config file (hibernate.cfg.xml in resources folder)
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        playerAccessor = new PlayerAccessor(sessionFactory);
        deckAccessor = new DeckAccessor(sessionFactory);
        cardAccessor = new CardAccessor(sessionFactory);
        cardGenerator = new CardProxy();
        cli = new CommandLineInterface(System.in, System.out);
    }

    /**
     * Create a new game with custom card generation, command line interface, and existing session factory
     * @param customCardGenerator Custom card generator implementation to get around calling the API
     * @param commandLineInterface Custom command line interface to get input from other sources
     * @param sessionFactory Existing session factory to use for accessing H2
     */
    public Game(CardGenerator customCardGenerator, CommandLineInterface commandLineInterface, SessionFactory sessionFactory) {
        playerAccessor = new PlayerAccessor(sessionFactory);
        deckAccessor = new DeckAccessor(sessionFactory);
        cardAccessor = new CardAccessor(sessionFactory);
        cardGenerator = customCardGenerator;
        cli = commandLineInterface;
    }

    /**
     * Main application/game loop
     */
    public void play() {
        cli.printLine(getWelcome());
        cli.printLine(getHelp());
        while (true) {
            String input = cli.getNextLine();
            LOGGER.info("User input: {}", input);
            switch (input.split(" ")[0]) {
                case "create_player" -> createPlayer(input);
                case "create_deck" -> createDeck(input);
                case "draw" -> draw(input);
                case "print" -> print(input);
                case "exit", "!q" -> exit();
                case "help" -> help();
                default -> {
                    cli.printLine("Invalid command, use \"help\" for more info");
                    LOGGER.info("User entered invalid input, {}", input);
                }
            }
        }
    }

    /**
     * Functionality for the create_player command
     * @param input user input to the command
     */
    public void createPlayer(String input) {
        String [] uInputs = input.split(" ");
        Player player = null;
        if (uInputs.length != 2) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        try {
            player = playerAccessor.createPlayer(uInputs[1]);
        } catch (IllegalArgumentException e) {
            cli.printLine(String.format("Could not create Player. %s: %s", e.getMessage(), uInputs[1]));
            return;
        }
        playerAccessor.persistPlayer(player);
        LOGGER.info("Valid input, created user {}: {}", player.getPlayerId(), player.getName());
        cli.printLine(String.format("Created player %d: %s", player.getPlayerId(), player.getName()));
    }

    /**
     * Functionality for the create_deck command
     * @param input user input to the command
     */
    public void createDeck(String input) {
        String[] uInputs = input.split(" ");
        Deck deck = null;
        if (uInputs.length != 3) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        Player player = playerAccessor.getPlayerByName(uInputs[1]);
        if (player == null) {
            cli.printLine(String.format("No player named: %s", uInputs[1]));
            return;
        }
        try {
            deck = deckAccessor.createDeck(uInputs[2], player, new ArrayList<>());
        } catch (IllegalArgumentException e) {
            cli.printLine(String.format("Could not create deck. %s: %s", e.getMessage(), uInputs[2]));
            return;
        }
        deckAccessor.persistDeck(deck);
        LOGGER.info("Valid input, created deck {} for user {} with name {}", deck.getDeckId(), player.getPlayerId(), deck.getName());
        cli.printLine(String.format("Created deck %d: %s for %s", deck.getDeckId(), deck.getName(), player.getName()));
    }


    /**
     * Functionality for the draw command
     * @param input user input to the command
     */
    public void draw(String input) {
        String [] uInputs = input.split(" ");
        if (uInputs.length != 3) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        Deck deck = deckAccessor.getDeckByPlayerNameAndDeckName(uInputs[1], uInputs[2]);
        if (deck == null) {
            cli.printLine(String.format("No deck: %s, for player %s", uInputs[2], uInputs[1]));
            return;
        }
        Card card = cardGenerator.getRandomCard();
        cli.printLine("You drew...");
        cli.printLine(card.getCardDescription());
        cli.printLine("Do you want to add this card to your deck? Y/N");
        String choice;
        boolean gettingInput = true;
        while (gettingInput) {
            choice = cli.getNextLine();
            switch (choice.split(" ")[0]) {
                case "Y", "y", "Yes", "yes", "YES" -> {
                    deck.addCards(card);
                    cardAccessor.persistCard(card);
                    deckAccessor.updateDeck(deck);
                    cli.printLine("Card saved");
                    gettingInput = false;
                }
                case "N", "n", "No", "no", "NO" -> {
                    cli.printLine("Card not saved");
                    gettingInput = false;
                }
                default -> {
                    cli.printLine("Invalid option please input Yes or No");
                    gettingInput = false;
                }
            }
        }
    }

    /**
     * Functionality for the print command
     * @param input user input to the command
     */
    public void print(String input) {
        String [] uInputs = input.split(" ");
        if (uInputs.length != 2) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        Player player = playerAccessor.getPlayerByName(uInputs[1]);
        if (player == null) {
            cli.printLine(String.format("No player named: %s", uInputs[1]));
            return;
        }
        cli.printLine(player.toString());
    }

    /**
     * Functionality for the exit command
     */
    public void exit() {
        LOGGER.info("User quitting application.");
        System.exit(0);
    }

    /**
     * Functionality for the help command
     */
    public void help() {
        cli.printLine(getHelp());
    }


    /**
     * Get the welcome string
     * @return welcome string for game
     */
    private String getWelcome() {
        return """
                ######################################################
                             Welcome to Yu-Gi-Oh! Clone App
                ######################################################""";
    }

    /**
     * Get the help string
     * @return help string for game
     */
    private String getHelp() {
        return """
                Available Commands:
                "create_player <name>" to create a new player
                "create_deck <player_name> <deck_name>" create a deck with <deck_name> for player <player_name>
                "draw <player_name> <deck_name>" draw a random card to add to deck
                "print <player_name>" print player by name
                "exit", "!q" to quit
                "help" print this help text""";
    }
}
