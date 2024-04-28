Feature: U3 - As Alex, I want to be able to be able to battle against other players

  Scenario: AC1 - I can play a game against another player
    Given A player "Alex" exists with a valid battle deck
    And A player "Carol" exists with a valid battle deck
    When I, "Alex", battle "Carol"
    Then We battle?

  Scenario Outline: AC2 - Each player in the game must have a valid battle deck prepared to play
    Given A player <player1_name> exists <player1_battle_deck> a valid battle deck
    And A player <player2_name> exists <player2_battle_deck> a valid battle deck
    When I, <player1_name>, battle <player2_name>
    Then I am informed that the battle could not start for reason <reason>
    Examples:
      | player1_name | player1_battle_deck | player2_name | player2_battle_deck | reason                                           |
      | "Alex"       | with                | "Carol"      | without             | "Player Carol does not have a valid battle deck" |
      | "Alex"       | without             | "Carol"      | with                | "Player Alex does not have a valid battle deck"  |

  Scenario: AC3 - A game is concluded once all turns are up and the winner is decided as the player with the most total
  Monster health remaining
    Given A player "Alex" exists with a valid battle deck
    And A player "Carol" exists with a valid battle deck
    When I, "Alex", battle "Carol"
    Then The battle goes on for 10 turns
    And A winner is decided or the game ends in a draw

  Scenario: AC4 - I cannot play a game against a player that doesn't exist
    Given A player "Alex" exists with a valid battle deck
    When I, "Alex", battle "A_PLAYER_THAT_DOESNT_EXIST"
    Then I am informed that the battle could not start for reason "No player named: A_PLAYER_THAT_DOESNT_EXIST"


