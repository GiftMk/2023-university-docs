package uc.seng301.cardbattler.lab6.cards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import uc.seng301.cardbattler.lab6.model.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Card API fetching functionality, makes use of {@link CardResponse} and Jackson to map the returned JSON to a card
 */
public class CardService implements CardGenerator {
    private static final Logger LOGGER = LogManager.getLogger(CardService.class);
    private static final String CARD_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php?id=";
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private List<Integer> monsterIds;
    private Random random;

    /**
     * Create a new card service
     */
    public CardService() {
        monsterIds = getMonsterIds();
        random = new Random();
    }

    @Override
    public Card getRandomCard() {
        String apiResponse = getResponseFromAPI();
        if (apiResponse != null && !apiResponse.isEmpty()) {
            CardResponse cardResponse = parseResponse(apiResponse);
            if (cardResponse != null) {
                return cardResponse.toCard();
            }
        }
        return getOfflineResponse().toCard();
    }

    /**
     * Gets the response from the API
     * @return The response body of the request as a String
     */
    public String getResponseFromAPI() {
        String data = null;
        try {
            int randomId = random.nextInt(monsterIds.size());
            LOGGER.info("Fetching card with id: {}", randomId);
            URL url = new URL(CARD_URL + monsterIds.get(randomId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            LOGGER.info("Api responded with status code: {}", responseCode);

            if (responseCode == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder stringResult = new StringBuilder();
                while (scanner.hasNext()) {
                    stringResult.append(scanner.nextLine());
                }
                data = stringResult.toString();
                scanner.close();
            } else {
                LOGGER.error("unable to process request to API, response code is '{}'", responseCode);
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing API response", e);
        }
        return data;
    }

    /**
     * Parse the json response to a {@link CardResponse} object using Jackson
     * @param stringResult String representation of response body (JSON)
     * @return CardResponse decoded from string
     */
    private CardResponse parseResponse(String stringResult) {
        CardResponse cardResponse = null;
        try {
            String cardData = stringResult.substring(9, stringResult.length() - 2);
            cardResponse = objectMapper.readValue(cardData, CardResponse.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing API response", e);
        }
        return cardResponse;
    }

    /**
     * Loads card response from a text file if there is an issue with the API (e.g. there is no internet connection, or the API is down)
     * If there is an error loading cards from this file the application will exit as the state will be unusable
     *
     * @return {@link CardResponse} object with values manually assigned to those loaded from a file
     */
    private CardResponse getOfflineResponse() {
        LOGGER.warn("Falling back to offline cards");
        List<CardResponse> cardResponses = null;
        try {
            cardResponses = objectMapper.readValue(new File(getClass().getClassLoader().getResource("all_monsters.json").toURI()), new TypeReference<>() {
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            LOGGER.fatal("ERROR parsing offline data, app is now exiting as no further functionality wil work");
            System.exit(1);
        }
        return cardResponses.get(random.nextInt(cardResponses.size()));
    }


    /**
     * Gets list of ids for "Normal Monsters" that can be used to query the api
     * WARNING: Does not work in jar
     *
     * @return List of API ids for "Normal Monsters"
     */
    private List<Integer> getMonsterIds() {
        List<Integer> ids = new ArrayList<>();
        try {
            File myObj = new File(getClass().getClassLoader().getResource("monster_ids.txt").toURI());
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    ids.add(Integer.parseInt(data));
                }
            }
        } catch (FileNotFoundException | URISyntaxException e) {
            LOGGER.error("Could not load monster ids");
            e.printStackTrace();
        } catch (NullPointerException ignored) {
            LOGGER.error("Could not load monster_ids.txt are you running a test?");
        }
        return ids;
    }

}
