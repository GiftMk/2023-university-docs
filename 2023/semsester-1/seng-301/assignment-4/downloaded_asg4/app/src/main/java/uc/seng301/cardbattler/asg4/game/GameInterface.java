package uc.seng301.cardbattler.asg4.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uc.seng301.cardbattler.asg4.accessor.DeckAccessor;
import uc.seng301.cardbattler.asg4.accessor.PlayerAccessor;
import uc.seng301.cardbattler.asg4.cards.CardGenerator;
import uc.seng301.cardbattler.asg4.cards.CardProxy;
import uc.seng301.cardbattler.asg4.cli.CommandLineInterface;
import uc.seng301.cardbattler.asg4.model.Deck;
import uc.seng301.cardbattler.asg4.model.Player;

/**
 * Main game loop functionality for application
 */
public class GameInterface {
    private static final Logger LOGGER = LogManager.getLogger(GameInterface.class);
    private final CommandLineInterface cli;
    private final PlayerAccessor playerAccessor;
    private final DeckAccessor deckAccessor;
    private final CardGenerator cardGenerator;
    private final BattleDeckCreator battleDeckCreator;

    private String welcomeMessage = """
            ######################################################
                         Welcome to Yu-Gi-Oh! Clone App
            ######################################################""";

    private String helpMessage = """
            Available Commands:
            "create_player <name> <playstyle>" to create a new AI player with style (i.e. 'basic', 'monster', 'setup', 'reckless')
            "battle_deck <player_name> <deck_name>" create a battle deck (A player can only have 1 deck, will override existing)
            "play_game <player1_name> <player2_name>" to play a game between two AI players
            "print <player_name>" print player by name
            "exit", "!q" to quit
            "help" print this help text""";

    /**
     * Create a new game with default settings
     */
    public GameInterface() {
        // this will load the config file (hibernate.cfg.xml in resources folder)
        playerAccessor = new PlayerAccessor();
        deckAccessor = new DeckAccessor();
        cardGenerator = new CardProxy();
        battleDeckCreator = new BattleDeckCreator(cardGenerator);
        cli = new CommandLineInterface(System.in, System.out);
    }

    /**
     * Create a new game with custom card generation, command line interface, and
     * existing session factory
     *
     * @param customCardGenerator  Custom card generator implementation to get
     *                             around calling the API
     * @param commandLineInterface Custom command line interface to get input from
     *                             other sources
     */
    public GameInterface(CardGenerator customCardGenerator, CommandLineInterface commandLineInterface) {
        playerAccessor = new PlayerAccessor();
        deckAccessor = new DeckAccessor();
        cardGenerator = customCardGenerator;
        battleDeckCreator = new BattleDeckCreator(cardGenerator);
        cli = commandLineInterface;
    }

    /**
     * Run a game
     */
    public void startGame() {
        cli.printLine(welcomeMessage);
        cli.printLine(helpMessage);
        play();
    }

    /**
     * Main application/game loop
     * 
     * @return true if the game should be ended
     */
    public void play() {
        boolean keepPlaying = true;
        while (keepPlaying) {
            String input = cli.getNextLine();
            LOGGER.info("User input: {}", input);
            switch (input.split(" ")[0]) {
                case "create_player" -> createPlayer(input);
                case "battle_deck" -> battleDeck(input);
                case "play_game" -> playGame(input);
                case "print" -> print(input);
                case "exit", "!q" -> {
                    LOGGER.info("User quitting application.");
                    cli.printLine("Bye!");
                    keepPlaying = false;
                }
                case "help" -> cli.printLine(helpMessage);
                default -> {
                    cli.printLine("Invalid command, use \"help\" for more info");
                    LOGGER.info("User entered invalid input, {}", input);
                }
            }
        }
    }

    /**
     * Functionality for the create_player command
     *
     * @param input user input to the command
     */
    private void createPlayer(String input) {
        String[] uInputs = input.split(" ");
        Player player = null;
        if (uInputs.length != 3) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        try {
            player = playerAccessor.createPlayer(uInputs[1], uInputs[2]);
        } catch (IllegalArgumentException e) {
            cli.printLine(String.format("Could not create Player. %s: %s", e.getMessage(), uInputs[1]));
            return;
        }
        playerAccessor.persistPlayer(player);
        LOGGER.info("Valid input, created player {}", player.getName());
        cli.printLine(String.format("Created player %s", player.getName()));
    }

    /**
     * Functionality to create a new battle deck
     * 
     * @param input user input to the command
     */
    public void battleDeck(String input) {
        String[] uInputs = input.split(" ");
        if (uInputs.length != 3) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        Deck deck;
        Player player = playerAccessor.getPlayerByName(uInputs[1]);
        if (player == null) {
            cli.printLine(String.format("No player named: %s", uInputs[1]));
            return;
        }
        if (player.getDeck() != null) {
            cli.printLine("A deck exists, overwriting...");
            return;
        }
        cli.printLine(
                "Do you want a random distribution (type 'random') or choose the number of each card (type 'choice')");
        String choice;
        boolean gettingInput = true;
        Map<String, Integer> cardFrequencyValues = new HashMap<>();
        while (gettingInput) {
            choice = cli.getNextLine();
            switch (choice.split(" ")[0].toLowerCase()) {
                case "random", "r" -> {
                    cardFrequencyValues = getCardFrequencyValues();
                    gettingInput = false;
                }
                case "choice", "c" -> {
                    cardFrequencyValues = getCustomCardFrequencyValues();
                    gettingInput = false;
                }
                default -> cli.printLine("Invalid option please input Random or Choice");
            }
        }
        try {
            deck = deckAccessor.createDeck(uInputs[2], player, new ArrayList<>());
        } catch (IllegalArgumentException e) {
            cli.printLine(String.format("Could not create deck. %s: %s", e.getMessage(), uInputs[2]));
            return;
        }
        cli.printLine("Accessing rate-limited API, operation may take a few seconds...");
        battleDeckCreator.populateRandomBattleDeck(deck, cardFrequencyValues.get("Monster"),
                cardFrequencyValues.get("Spell"), cardFrequencyValues.get("Trap"));
        cli.printLine("Deck created: " + deck.toString());
    }

    /**
     * Get custom frequencies for card types
     *
     * @return a map of cards types with the number of each of these cards to
     *         compose a deck
     */
    private Map<String, Integer> getCustomCardFrequencyValues() {
        Map<String, Integer> cardFrequencyValues = new HashMap<>();
        Map<String, Integer> options = getCardFrequencyValues();
        Iterator<Entry<String, Integer>> i = options.entrySet().iterator();
        cli.printLine(String.format("""
                Please enter the starting number of each card type you want (Monsters, Spells, Traps).
                The total must not exceed %d. If too few cards given, random cards will be added to make it to %d.""",
                BattleDeckCreator.DECK_SIZE, BattleDeckCreator.DECK_SIZE));
        while (i.hasNext()) {
            String choice;
            boolean gettingInput = true;
            Entry<String, Integer> cardEntry = i.next();
            while (gettingInput) {
                cli.printLine(String.format("How many %s cards do you want, minimum: %d", cardEntry.getKey(),
                        cardEntry.getValue()));
                choice = cli.getNextLine();
                try {
                    int numSelected = Integer.parseInt(choice);
                    if (numSelected < cardEntry.getValue()) {
                        cli.printLine(String.format("Cannot have less than %d %s cards", cardEntry.getValue(),
                                cardEntry.getKey()));
                    } else {
                        cardFrequencyValues.put(cardEntry.getKey(), numSelected);
                        gettingInput = false;
                    }
                } catch (NumberFormatException nfe) {
                    cli.printLine("Number expected");
                }
            }

            if (!i.hasNext() && cardFrequencyValues.values().stream().mapToInt(Integer::intValue)
                    .sum() > BattleDeckCreator.DECK_SIZE) {
                cli.printLine(String.format("Total must not exceed %d", BattleDeckCreator.DECK_SIZE));
                // reset card frequency values and iterator to ask for card numbers again
                i = options.entrySet().iterator();
            }
        }
        return cardFrequencyValues;
    }

    /**
     * Plays a game between two players
     * Both of which must exist and have a valid battle deck
     * 
     * @param input user input to the command
     */
    public void playGame(String input) {
        String[] uInputs = input.split(" ");
        if (uInputs.length != 3) {
            cli.printLine("Command incorrect use \"help\" for more information");
            return;
        }
        Player p1 = playerAccessor.getPlayerByName(uInputs[1]);
        if (p1 == null) {
            cli.printLine(String.format("No player named: %s", uInputs[1]));
            return;
        }
        if (!BattleDeckCreator.deckIsValid(p1.getDeck())) {
            cli.printLine(String.format("Player %s does not have a valid battle deck", uInputs[1]));
            return;
        }
        Player p2 = playerAccessor.getPlayerByName(uInputs[2]);
        if (p2 == null) {
            cli.printLine(String.format("No player named: %s", uInputs[2]));
            return;
        }
        if (!BattleDeckCreator.deckIsValid(p2.getDeck())) {
            cli.printLine(String.format("Player %s does not have a valid battle deck", uInputs[2]));
            return;
        }
        Game game = new Game(cli);
        game.addPlayers(p1, p2);
        game.startGame();
    }

    /**
     * Functionality for the print command
     *
     * @param input user input to the command
     */
    private void print(String input) {
        String[] uInputs = input.split(" ");
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
     * Get the default minimum frequency values to build a valid battle deck
     * 
     * @return a map of card types with their minimum number of cards for that type
     */
    private Map<String, Integer> getCardFrequencyValues() {
        // note that keys are sorted in a specific order
        Map<String, Integer> cardFrequencyValues = new LinkedHashMap<>();
        cardFrequencyValues.put("Monster", BattleDeckCreator.MIN_NUM_MONSTERS);
        cardFrequencyValues.put("Spell", BattleDeckCreator.MIN_NUM_SPELLS);
        cardFrequencyValues.put("Trap", BattleDeckCreator.MIN_NUM_TRAPS);
        return cardFrequencyValues;
    }

    /**
     * Gets the player accessor (stores current players in memory for simplicity)
     * 
     * @return player accessor
     */
    public PlayerAccessor getPlayerAccessor() {
        return playerAccessor;
    }

    /**
     * Gets the deck accessor (here for posterity)
     * 
     * @return deck accessor
     */
    public DeckAccessor getDeckAccessor() {
        return deckAccessor;
    }
}
